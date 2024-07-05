package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.SelectCategoryViewsMapper
import br.com.mob1st.features.finances.impl.infra.data.system.StringIdProvider
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import br.com.mob1st.tests.featuresutils.TestTimberTree
import com.appmattus.kotlinfixture.Fixture
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import timber.log.Timber
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SelectSuggestionsMapperTest {
    private lateinit var mapper: SelectSuggestionsMapper

    private lateinit var fixture: Fixture
    private lateinit var timberTree: TestTimberTree
    private lateinit var selectCategoryViewsMapper: SelectCategoryViewsMapper
    private lateinit var stringIdProvider: StringIdProvider

    @BeforeEach
    fun setUp() {
        stringIdProvider = mockk()
        timberTree = TestTimberTree()
        fixture = moduleFixture.new {
            factory<SelectSuggestions> {
                val id = moduleFixture<Long>()
                SelectSuggestions(
                    sug_id = id,
                    sug_name = "rent_or_mortgage",
                    cat_id = moduleFixture(),
                    cat_linked_suggestion_id = id,
                    cat_name = null,
                    cat_amount = null,
                    cat_is_expense = null,
                    cat_created_at = null,
                    frc_day_of_month = null,
                    src_month = null,
                    src_day = null,
                )
            }
        }
        selectCategoryViewsMapper = SelectCategoryViewsMapper
        mapper = SelectSuggestionsMapper(
            selectCategoryViewMapper = SelectCategoryViewsMapper,
            stringIdProvider = stringIdProvider,
        )
        Timber.plant(timberTree)
    }

    @AfterEach
    fun tearDown() {
        Timber.uproot(timberTree)
    }

    @Test
    fun `GIVEN a list of suggestions with linked categories WHEN map THEN assert they are grouped by suggestion id`() {
        // Given
        val fixtures = TestGroupingSuggestionsFixtures(fixture)
        every { stringIdProvider["rent_or_mortgage"] } returns 1
        every { stringIdProvider["gym"] } returns 2
        every { stringIdProvider["public_transport"] } returns 3
        every { stringIdProvider["health_insurance"] } returns 4

        // When
        val actual = mapper.map(
            query = fixtures.suggestionsWithLinkedCategory + fixtures.suggestionsWithoutLinkedCategory,
        )

        // Then
        assertEquals(
            fixtures.expected(
                nameSuggestion1 = 1,
                nameSuggestion2 = 2,
                nameSuggestion3 = 3,
                nameSuggestion4 = 4,
            ),
            actual,
        )
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable name WHEN map THEN assert suggestion is discarded And error is logged`() {
        every { stringIdProvider["discarded"] } returns 1
        every { stringIdProvider["persisted"] } returns 2
        val fixtures = TestDiscardNullFixtures(fixture)
        val discardedSuggestion = fixtures.suggestionWithCategory.copy(
            sug_name = "discarded",
        )
        val persistedSuggestion = fixtures.suggestionWithoutCategory.copy(sug_name = "persisted")
        val actual = mapper.map(listOf(discardedSuggestion, persistedSuggestion))
        assertEquals(fixtures.expected(2), actual)
        assertIs<IllegalStateException>(timberTree.logs[0].t)
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable is_expense WHEN map THEN assert suggestion is discarded And error is logged`() {
        every { stringIdProvider["discarded"] } returns 1
        every { stringIdProvider["persisted"] } returns 2
        val fixtures = TestDiscardNullFixtures(fixture)
        val discardedSuggestion = fixtures.suggestionWithCategory.copy(
            sug_name = "discarded",
        )
        val persistedSuggestion = fixtures.suggestionWithoutCategory.copy(sug_name = "persisted")
        val actual = mapper.map(
            listOf(discardedSuggestion, persistedSuggestion),
        )
        assertEquals(fixtures.expected(2), actual)
        assertIs<IllegalStateException>(timberTree.logs[0].t)
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable amount WHEN map THEN assert suggestion is discarded And error is logged`() {
        every { stringIdProvider["discarded"] } returns 1
        every { stringIdProvider["persisted"] } returns 2
        val fixtures = TestDiscardNullFixtures(fixture)
        val discardedSuggestion = fixtures.suggestionWithCategory.copy(
            sug_name = "discarded",
        )
        val persistedSuggestion = fixtures.suggestionWithoutCategory.copy(sug_name = "persisted")
        val actual = mapper.map(listOf(discardedSuggestion, persistedSuggestion))
        assertEquals(fixtures.expected(2), actual)
        assertIs<IllegalStateException>(timberTree.logs[0].t)
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with different linked suggestion WHEN map THEN assert suggestion is discarded And error is logged`() {
        every { stringIdProvider["discarded"] } returns 1
        every { stringIdProvider["persisted"] } returns 2
        val fixtures = TestDiscardNullFixtures(fixture)
        val discardedSuggestion = fixtures.suggestionWithCategory.copy(sug_name = "discarded")
        val persistedSuggestion = fixtures.suggestionWithoutCategory.copy(sug_name = "persisted")
        val actual = mapper.map(listOf(discardedSuggestion, persistedSuggestion))
        assertEquals(fixtures.expected(2), actual)
        assertIs<IllegalStateException>(timberTree.logs[0].t)
    }

    @Test
    fun `GIVEN a list of suggestions And a unknown name WHEN map THEN assert suggestion is discarded And warning is logged`() {
        every { stringIdProvider["unknown_name"] } returns null
        every { stringIdProvider["persisted"] } returns 2
        val fixtures = TestUnknownNameFixtures(fixture)
        val discardedSuggestion = fixtures.suggestionWithCategory.copy(
            sug_name = "unknown_name",
            src_month = 1,
            src_day = 12,
        )
        val persistedSuggestion = fixtures.suggestionWithoutCategory.copy(sug_name = "persisted")
        val actual = mapper.map(listOf(discardedSuggestion, persistedSuggestion))
        assertEquals(fixtures.expected(2), actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @Test
    fun `GIVEN a list of suggestions And two categories are linked to the same suggestion WHEN map THEN assert that only the first category is used And a warning is logged`() {
        every { stringIdProvider["first"] } returns 1
        every { stringIdProvider["second"] } returns 2
        every { stringIdProvider["third"] } returns 3

        val fixtures = TestTwoCategoriesWithSameSuggestionFixtures(fixture)
        val firstRegistryWithCategory = fixtures.firstRegistryWithCategory.copy(sug_name = "first")
        val secondRegistryWithCategory = fixtures.secondRegistryWithCategory.copy(sug_name = "second")

        val actual = mapper.map(
            listOf(firstRegistryWithCategory, secondRegistryWithCategory, fixtures.thirdRegistryNoCategory),
        )
        assertEquals(
            fixtures.expected(
                recurrenceSuggestion1 = Recurrences.Variable,
                nameResIdSuggestion1 = 1,
                nameResIdSuggestion3 = 3,
            ),
            actual,
        )
        assertTrue(timberTree.logs[0].isWarning)
    }
}
