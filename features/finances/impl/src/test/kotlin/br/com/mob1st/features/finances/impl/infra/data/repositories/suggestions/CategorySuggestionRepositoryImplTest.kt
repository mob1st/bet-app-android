package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.androidx.assets.AssetsGetter
import br.com.mob1st.core.androidx.resources.StringIdGetter
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.tests.featuresutils.TestTimberTree
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
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
import org.junit.jupiter.api.io.TempDir
import timber.log.Timber
import java.io.File
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CategorySuggestionRepositoryImplTest {
    private lateinit var repository: CategorySuggestionsRepositoryImpl
    private lateinit var stringIdGetter: StringIdGetter
    private lateinit var assetsGetter: AssetsGetter
    private lateinit var suggestionListPerStep: SuggestionListPerStep
    private lateinit var timberTree: TestTimberTree
    private val io = IoCoroutineDispatcher(
        UnconfinedTestDispatcher(),
    )

    @BeforeEach
    fun setUp() {
        timberTree = TestTimberTree()
        Timber.plant(timberTree)
        suggestionListPerStep = mockk()
        stringIdGetter = mockk()
        assetsGetter = mockk()
        repository = CategorySuggestionsRepositoryImpl(
            io = io,
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
    fun `GIVEN an invalid name WHEN get by step THEN assert invalid is skipped And warning is logged`(
        @TempDir tempDir: File,
    ) = runTest {
        // Given
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val validFile = tempDir.resolve("valid.svg").apply {
            createNewFile()
        }
        val invalidFile = tempDir.resolve("invalid.svg").apply {
            createNewFile()
        }

        every { suggestionListPerStep[step] } returns listOf("valid", "invalid")
        givenSuggestion("valid", "valid", validFile)
        givenSuggestion("invalid", null, invalidFile)

        // When
        val actual = repository.getByStep(step).first()

        // Then
        val expected = CategorySuggestion(name = "valid", image = Uri(validFile.absolutePath))
        assertEquals(listOf(expected), actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @Test
    fun `GIVEN a non existent file WHEN get by step THEN assert suggestion is skipped And warning is logged`(
        @TempDir tempDir: File,
    ) = runTest {
        // Given
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val existentFile = tempDir.resolve("existent.svg").apply {
            createNewFile()
        }
        val nonExistentFile = tempDir.resolve("non_existent.svg")

        every { suggestionListPerStep[step] } returns listOf("existent", "non_existent")
        givenSuggestion("existent", "existent", existentFile)
        givenSuggestion("non_existent", "non_existent", nonExistentFile)

        // When
        val actual = repository.getByStep(step).first()

        // Then
        val expected = CategorySuggestion(name = "existent", image = Uri(existentFile.absolutePath))
        assertEquals(listOf(expected), actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @Test
    fun `GIVEN a non existent file And a invalid res id WHEN get by step THEN assert suggestion is skipped And warning is logged`(
        @TempDir tempDir: File,
    ) = runTest {
        // Given
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val nonExistent = tempDir.resolve("persisted.svg").apply {
            createNewFile()
        }
        val invalidFile = tempDir.resolve("skipped.svg")

        every { suggestionListPerStep[step] } returns listOf("persisted", "skipped")
        givenSuggestion("persisted", "persisted", nonExistent)
        givenSuggestion("skipped", null, invalidFile)

        // When
        val actual = repository.getByStep(step).first()

        // Then
        val expected = CategorySuggestion(name = "persisted", image = Uri(nonExistent.absolutePath))
        assertEquals(listOf(expected), actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @Test
    fun `GIVEN a valid file And a valid name WHEN get by step THEN assert suggestions are returned And no log happens`(
        @TempDir tempDir: File,
    ) = runTest {
        // Given
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val file1 = tempDir.resolve("file1.svg").apply {
            createNewFile()
        }
        val file2 = tempDir.resolve("file2.svg").apply {
            createNewFile()
        }
        val name1 = "name1"
        val name2 = "name2"

        every { suggestionListPerStep[step] } returns listOf("id1", "id2")
        givenSuggestion("id1", name1, file1)
        givenSuggestion("id2", name2, file2)

        // When
        val actual = repository.getByStep(step).first()

        // Then
        val expected1 = CategorySuggestion(name = name1, image = Uri(file1.absolutePath))
        val expected2 = CategorySuggestion(name = name2, image = Uri(file2.absolutePath))
        assertEquals(listOf(expected1, expected2), actual)
        assertTrue(timberTree.logs.isEmpty())
    }

    private fun givenSuggestion(
        id: String,
        name: String?,
        file: File,
    ) {
        every { stringIdGetter.getString(id) } returns name
        every { assetsGetter["icons/$id.svg"] } returns file
    }
}
