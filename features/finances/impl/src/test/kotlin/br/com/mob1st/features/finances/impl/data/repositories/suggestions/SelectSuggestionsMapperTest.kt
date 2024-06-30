package br.com.mob1st.features.finances.impl.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.data.repositories.categories.SelectCategoryViewsMapper
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import br.com.mob1st.tests.featuresutils.TestTimberTree
import com.appmattus.kotlinfixture.Fixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import timber.log.Timber
import java.util.stream.Stream
import kotlin.test.assertEquals

class SelectSuggestionsMapperTest {
    private lateinit var mapper: SelectSuggestionsMapper

    private lateinit var fixture: Fixture
    private lateinit var selectCategoryViewsMapper: SelectCategoryViewsMapper

    @BeforeEach
    fun setUp() {
        Timber.plant(TestTimberTree())
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
                    vrc_day_of_week = null,
                    src_month = null,
                    src_day = null,
                )
            }
        }
        selectCategoryViewsMapper = SelectCategoryViewsMapper
        mapper = SelectSuggestionsMapper(selectCategoryViewsMapper)
    }

    @Test
    fun `GIVEN a list of suggestions with linked categories WHEN map THEN assert they are grouped by suggestion id`() {
        // Given
        val suggestions = listOfSuggestionsWithFixedCategory() + listOfSuggestionsWithoutLinkedCategory()
        val linkedCategories = linkedFixedCategories()

        // When
        val actual = mapper.map(CategoryType.Fixed, suggestions)

        // Then
        val expected = listOf(
            CategorySuggestion(
                id = RowId(1),
                name = CategorySuggestion.Name.RentOrMortgage,
                linkedCategory = linkedCategories.first(),
            ),
            CategorySuggestion(
                id = RowId(2),
                name = CategorySuggestion.Name.Gym,
                linkedCategory = linkedCategories.last(),
            ),
            CategorySuggestion(
                id = RowId(3),
                name = CategorySuggestion.Name.PublicTransport,
                linkedCategory = null,
            ),
            CategorySuggestion(
                id = RowId(4),
                name = CategorySuggestion.Name.HealthInsurance,
                linkedCategory = null,
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable name WHEN map THEN assert suggestion is discarded And error is logged`() {
        val discardedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = "night_clubs",
            cat_name = null,
            cat_id = 1,
            cat_is_expense = true,
            cat_amount = 300_000,
            cat_linked_suggestion_id = 1,
            vrc_day_of_week = 3,
        )
        val persistedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = "bars",
            cat_id = null,
        )
        val actual = mapper.map(CategoryType.Variable, listOf(discardedSuggestion, persistedSuggestion))
        val expected = listOf(
            CategorySuggestion(
                id = RowId(2),
                name = CategorySuggestion.Name.Bars,
                linkedCategory = null,
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable is_expense WHEN map THEN assert suggestion is discarded And error is logged`() {
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable amount WHEN map THEN assert suggestion is discarded And error is logged`() {
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable created_at WHEN map THEN assert empty is used as placeholder for created_at`() {
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with different linked suggestion WHEN map THEN assert suggestion is discarded And error is logged`() {
    }

    @Test
    fun `GIVEN a list of suggestions And a unknown name WHEN map THEN assert suggestion is discarded And message is logged`() {
    }

    @Test
    fun `GIVEN a list of suggestions And two categories are linked to the same suggestion WHEN map THEN assert that only the first category is used And a log message is added`() {
    }

    @ParameterizedTest
    @ArgumentsSource(SuggestionsNameProvider::class)
    fun `GIVEN a list of suggestions WHEN map THEN assert the names are mapped correctly`() {
    }

    private fun linkedFixedCategories(): List<Category> {
        val category1 = Category(
            id = RowId(1),
            name = "Rent",
            isExpense = true,
            amount = Money(3000_00),
            recurrences = Recurrences.Fixed(
                listOf(
                    DayOfMonth(1),
                    DayOfMonth(2),
                ),
            ),
        )
        val category2 = Category(
            id = RowId(2),
            name = "Gym",
            isExpense = true,
            amount = Money(25_00),
            recurrences = Recurrences.Fixed(
                listOf(
                    DayOfMonth(3),
                ),
            ),
        )
        return listOf(category1, category2)
    }

    private fun listOfSuggestionsWithFixedCategory() = listOf(
        fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = "rent_or_mortgage",
            cat_name = "Rent",
            cat_id = 1,
            cat_is_expense = true,
            cat_amount = 300_000,
            cat_linked_suggestion_id = 1,
            frc_day_of_month = 1,
        ),
        fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = "rent_or_mortgage",
            cat_name = "Rent",
            cat_id = 1,
            cat_is_expense = true,
            cat_amount = 300_000,
            cat_linked_suggestion_id = 1,
            frc_day_of_month = 2,
        ),
        fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = "gym",
            cat_name = "Gym",
            cat_id = 2,
            cat_linked_suggestion_id = 2,
            cat_amount = 2500,
            cat_is_expense = true,
            frc_day_of_month = 3,
        ),
    )

    private fun listOfSuggestionsWithoutLinkedCategory() = listOf(
        fixture<SelectSuggestions>().copy(
            sug_id = 3,
            sug_name = "public_transport",
            cat_id = null,
        ),
        fixture<SelectSuggestions>().copy(
            sug_id = 4,
            sug_name = "health_insurance",
            cat_id = null,
        ),
    )

    object SuggestionsNameProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            TODO("Not yet implemented")
        }
    }

    companion object {
    }
}
