package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.androidx.assets.AssetsGetter
import br.com.mob1st.core.androidx.resources.StringIdGetter
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.tests.featuresutils.TestTimberTree
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import timber.log.Timber
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CategorySuggestionRepositoryImplTest {
    private lateinit var repository: CategorySuggestionsRepositoryImpl
    private lateinit var stringIdGetter: StringIdGetter
    private lateinit var assetsGetter: AssetsGetter
    private lateinit var suggestionListPerStep: SuggestionListPerStep
    private lateinit var timberTree: TestTimberTree

    @BeforeEach
    fun setUp() {
        timberTree = TestTimberTree()
        Timber.plant(timberTree)
        suggestionListPerStep = mockk()
        stringIdGetter = mockk()
        assetsGetter = mockk()
        repository = CategorySuggestionsRepositoryImpl(
            default = DefaultCoroutineDispatcher(UnconfinedTestDispatcher()),
            stringIdGetter = stringIdGetter,
            assetsGetter = assetsGetter,
            suggestionListPerStep = suggestionListPerStep,
        )
    }

    @AfterEach
    fun tearDown() {
        Timber.uproot(timberTree)
    }

    @Test
    fun `GIVEN an invalid name WHEN get by step THEN assert invalid is skipped And warning is logged`() = runTest {
        // Given
        val step = Arb.bind<BudgetBuilderAction.Step>().next()

        every { suggestionListPerStep[step] } returns listOf("valid_id", "invalid_id")
        givenSuggestion("valid_id", "valid", "valid.svg")
        givenSuggestion("invalid_id", null, "invalid.svg")

        // When
        val actual = repository.getByStep(step).first()

        // Then
        val expected = CategorySuggestion(name = "valid", image = Uri("valid.svg"))
        assertEquals(listOf(expected), actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @Test
    fun `GIVEN a non existent file WHEN get by step THEN assert suggestion is skipped And warning is logged`() = runTest {
        // Given
        val step = Arb.bind<BudgetBuilderAction.Step>().next()

        every { suggestionListPerStep[step] } returns listOf("valid_id", "invalid_id")
        givenSuggestion("valid_id", "valid", "valid.svg")
        givenSuggestion("invalid_id", "invalid", null)

        // When
        val actual = repository.getByStep(step).first()

        // Then
        val expected = CategorySuggestion(name = "valid", image = Uri("valid.svg"))
        assertEquals(listOf(expected), actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @Test
    fun `GIVEN a non existent file And a invalid res id WHEN get by step THEN assert suggestion is skipped And warning is logged`() = runTest {
        // Given
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        every { suggestionListPerStep[step] } returns listOf("valid_id", "invalid_id")
        givenSuggestion("valid_id", "valid", "valid.svg")
        givenSuggestion("invalid_id", null, null)

        // When
        val actual = repository.getByStep(step).first()

        // Then
        val expected = CategorySuggestion(name = "valid", image = Uri("valid.svg"))
        assertEquals(listOf(expected), actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @Test
    fun `GIVEN a valid file And a valid name WHEN get by step THEN assert suggestions are returned And no log happens`() = runTest {
        // Given
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        every { suggestionListPerStep[step] } returns listOf("valid_id1", "valid_id2")
        givenSuggestion("valid_id1", "valid1", "valid1.svg")
        givenSuggestion("valid_id2", "valid2", "valid2.svg")

        // When
        val actual = repository.getByStep(step).first()

        // Then
        val expected1 = CategorySuggestion(name = "valid1", image = Uri("valid1.svg"))
        val expected2 = CategorySuggestion(name = "valid2", image = Uri("valid2.svg"))
        assertEquals(listOf(expected1, expected2), actual)
        assertTrue(timberTree.logs.isEmpty())
    }

    private fun givenSuggestion(
        id: String,
        name: String?,
        file: String?,
    ) {
        every { stringIdGetter.getString(id) } returns name
        coEvery { assetsGetter.get("icons/$id.svg") } returns file
    }
}
