package br.com.mob1st.features.finances.impl.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.data.repositories.categories.SelectCategoryViewsMapper
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfWeek
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import br.com.mob1st.tests.featuresutils.TestTimberTree
import com.appmattus.kotlinfixture.Fixture
import org.junit.jupiter.api.AfterEach
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
import kotlin.test.assertTrue

class SelectSuggestionsMapperTest {
    private lateinit var mapper: SelectSuggestionsMapper

    private lateinit var fixture: Fixture
    private lateinit var timberTree: TestTimberTree
    private lateinit var selectCategoryViewsMapper: SelectCategoryViewsMapper

    @BeforeEach
    fun setUp() {
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
                    vrc_day_of_week = null,
                    src_month = null,
                    src_day = null,
                )
            }
        }
        selectCategoryViewsMapper = SelectCategoryViewsMapper
        mapper = SelectSuggestionsMapper(selectCategoryViewsMapper)
        Timber.plant(timberTree)
    }

    @AfterEach
    fun tearDown() {
        Timber.uproot(timberTree)
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
            sug_name = allSuggestionsNames.random().first,
            cat_name = null,
            cat_id = 1,
            cat_is_expense = true,
            cat_amount = 300_000,
            cat_linked_suggestion_id = 1,
            vrc_day_of_week = 3,
        )
        val name = allSuggestionsNames.random()
        val persistedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = name.first,
            cat_id = null,
        )
        val actual = mapper.map(CategoryType.Variable, listOf(discardedSuggestion, persistedSuggestion))
        val expected = listOf(
            CategorySuggestion(
                id = RowId(2),
                name = name.second,
                linkedCategory = null,
            ),
        )
        assertEquals(expected, actual)
        assertTrue(timberTree.logs[0].isError)
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable is_expense WHEN map THEN assert suggestion is discarded And error is logged`() {
        val discardedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = allSuggestionsNames.random().first,
            cat_id = 1,
            cat_is_expense = null,
            cat_amount = 1000_00,
            cat_linked_suggestion_id = 1,
            src_month = 1,
            src_day = 12,
        )
        val name = allSuggestionsNames.random()
        val persistedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = name.first,
            cat_id = null,
        )
        val actual = mapper.map(CategoryType.Seasonal, listOf(discardedSuggestion, persistedSuggestion))
        val expected = listOf(
            CategorySuggestion(
                id = RowId(2),
                name = name.second,
                linkedCategory = null,
            ),
        )
        assertEquals(expected, actual)
        assertTrue(timberTree.logs[0].isError)
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with nullable amount WHEN map THEN assert suggestion is discarded And error is logged`() {
        val discardedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = allSuggestionsNames.random().first,
            cat_id = 1,
            cat_is_expense = true,
            cat_amount = null,
            cat_linked_suggestion_id = 1,
            vrc_day_of_week = 3,
        )
        val name = allSuggestionsNames.random()
        val persistedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = name.first,
            cat_id = null,
        )
        val actual = mapper.map(CategoryType.Variable, listOf(discardedSuggestion, persistedSuggestion))
        val expected = listOf(
            CategorySuggestion(
                id = RowId(2),
                name = name.second,
                linkedCategory = null,
            ),
        )
        assertEquals(expected, actual)
        assertTrue(timberTree.logs[0].isError)
    }

    @Test
    fun `GIVEN a list of suggestions And a linked category with different linked suggestion WHEN map THEN assert suggestion is discarded And error is logged`() {
        val discardedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = allSuggestionsNames.random().first,
            cat_id = 1,
            cat_is_expense = true,
            cat_amount = 1000_00,
            cat_linked_suggestion_id = 2,
            src_month = 1,
            src_day = 12,
        )
        val name = allSuggestionsNames.random()
        val persistedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = name.first,
            cat_id = null,
        )
        val actual = mapper.map(CategoryType.Seasonal, listOf(discardedSuggestion, persistedSuggestion))
        val expected = listOf(
            CategorySuggestion(
                id = RowId(2),
                name = name.second,
                linkedCategory = null,
            ),
        )
        assertEquals(expected, actual)
        assertTrue(timberTree.logs[0].isError)
    }

    @Test
    fun `GIVEN a list of suggestions And a unknown name WHEN map THEN assert suggestion is discarded And warning is logged`() {
        val discardedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = "unknown_name",
            cat_id = 1,
            cat_is_expense = true,
            cat_amount = 1000_00,
            cat_linked_suggestion_id = 1,
            src_month = 1,
            src_day = 12,
        )
        val name = allSuggestionsNames.random()
        val persistedSuggestion = fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = name.first,
            cat_id = null,
        )
        val actual = mapper.map(CategoryType.Seasonal, listOf(discardedSuggestion, persistedSuggestion))
        val expected = listOf(
            CategorySuggestion(
                id = RowId(2),
                name = name.second,
                linkedCategory = null,
            ),
        )
        assertEquals(expected, actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @Test
    fun `GIVEN a list of suggestions And two categories are linked to the same suggestion WHEN map THEN assert that only the first category is used And a warning is logged`() {
        val firstSuggestionName = allSuggestionsNames.random()
        val secondSuggestionName = allSuggestionsNames.random()
        val firstSuggestionWithCategory = fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = firstSuggestionName.first,
            cat_id = 1,
            cat_name = "any1",
            cat_is_expense = true,
            cat_amount = 1000_00,
            cat_linked_suggestion_id = 1,
            vrc_day_of_week = 4,
        )
        val secondSuggestionWithCategory = fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = firstSuggestionName.first,
            cat_id = 2,
            cat_name = "any2",
            cat_is_expense = true,
            cat_amount = 2000_00,
            cat_linked_suggestion_id = 1,
            vrc_day_of_week = 3,
        )
        val suggestionWithoutLinkedCategory = fixture<SelectSuggestions>().copy(
            sug_id = 2,
            sug_name = secondSuggestionName.first,
            cat_id = null,
        )
        val actual = mapper.map(
            CategoryType.Variable,
            listOf(firstSuggestionWithCategory, secondSuggestionWithCategory, suggestionWithoutLinkedCategory),
        )
        val expected = listOf(
            CategorySuggestion(
                id = RowId(1),
                name = firstSuggestionName.second,
                linkedCategory = Category(
                    id = RowId(1),
                    name = "any1",
                    isExpense = true,
                    amount = Money(1000_00),
                    recurrences = Recurrences.Variable(
                        daysOfWeek = listOf(DayOfWeek(4)),
                    ),
                ),
            ),
            CategorySuggestion(
                id = RowId(2),
                name = secondSuggestionName.second,
                linkedCategory = null,
            ),
        )
        assertEquals(expected, actual)
        assertTrue(timberTree.logs[0].isWarning)
    }

    @ParameterizedTest
    @ArgumentsSource(SuggestionsNameProvider::class)
    fun `GIVEN suggestion with a name as string WHEN map THEN assert the name are mapped to enum`(
        stringName: String,
        enumName: CategorySuggestion.Name,
    ) {
        val suggestion = fixture<SelectSuggestions>().copy(
            sug_id = 1,
            sug_name = stringName,
            cat_id = 1,
            cat_name = "any",
            cat_is_expense = false,
            cat_amount = 1000_00,
            cat_linked_suggestion_id = 1,
            frc_day_of_month = 2,
        )
        val actual = mapper.map(CategoryType.Fixed, listOf(suggestion))
        assertEquals(
            enumName,
            actual.first().name,
        )
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
            return allSuggestionsNames.map { (stringName, enumName) ->
                Arguments.of(stringName, enumName)
            }.stream()
        }
    }

    companion object {
        val allSuggestionsNames = listOf(
            "rent_or_mortgage" to CategorySuggestion.Name.RentOrMortgage,
            "property_taxes" to CategorySuggestion.Name.PropertyTaxes,
            "health_insurance" to CategorySuggestion.Name.HealthInsurance,
            "car_insurance" to CategorySuggestion.Name.CarInsurance,
            "public_transport" to CategorySuggestion.Name.PublicTransport,
            "home_insurance" to CategorySuggestion.Name.HomeInsurance,
            "loan_payments" to CategorySuggestion.Name.LoanPayments,
            "internet_subscription" to CategorySuggestion.Name.InternetSubscription,
            "cell_phone_plan" to CategorySuggestion.Name.CellPhonePlan,
            "cable_or_streaming_services" to CategorySuggestion.Name.CableOrStreamingServices,
            "music_streaming_services" to CategorySuggestion.Name.MusicStreamingServices,
            "magazine_or_newspaper_subscriptions" to CategorySuggestion.Name.MagazineOrNewspaperSubscriptions,
            "association_fees" to CategorySuggestion.Name.AssociationFees,
            "private_retirement_plans" to CategorySuggestion.Name.PrivateRetirementPlans,
            "personal_education" to CategorySuggestion.Name.PersonalEducation,
            "children_school" to CategorySuggestion.Name.ChildrenSchool,
            "childcare" to CategorySuggestion.Name.Childcare,
            "groceries" to CategorySuggestion.Name.Groceries,
            "dining_out" to CategorySuggestion.Name.DiningOut,
            "food_delivery" to CategorySuggestion.Name.FoodDelivery,
            "weekday_lunch" to CategorySuggestion.Name.WeekdayLunch,
            "coffee_snacks" to CategorySuggestion.Name.CoffeeSnacks,
            "transportation_fuel" to CategorySuggestion.Name.TransportationFuel,
            "public_transport_tickets" to CategorySuggestion.Name.PublicTransportTickets,
            "cinema" to CategorySuggestion.Name.Cinema,
            "sports_tickets" to CategorySuggestion.Name.SportsTickets,
            "electronic_games" to CategorySuggestion.Name.ElectronicGames,
            "bars" to CategorySuggestion.Name.Bars,
            "night_clubs" to CategorySuggestion.Name.NightClubs,
            "household_supplies" to CategorySuggestion.Name.HouseholdSupplies,
            "fitness_recreation" to CategorySuggestion.Name.FitnessRecreation,
            "holiday_gifts" to CategorySuggestion.Name.HolidayGifts,
            "vacation_travel" to CategorySuggestion.Name.VacationTravel,
            "back_to_school_supplies" to CategorySuggestion.Name.BackToSchoolSupplies,
            "winter_clothing" to CategorySuggestion.Name.WinterClothing,
            "summer_activities" to CategorySuggestion.Name.SummerActivities,
            "garden_supplies" to CategorySuggestion.Name.GardenSupplies,
            "home_heating" to CategorySuggestion.Name.HomeHeating,
            "holiday_decorations" to CategorySuggestion.Name.HolidayDecorations,
            "tax_preparation_fees" to CategorySuggestion.Name.TaxPreparationFees,
            "spring_cleaning" to CategorySuggestion.Name.SpringCleaning,
            "salary" to CategorySuggestion.Name.Salary,
            "pension" to CategorySuggestion.Name.Pension,
            "rental_income" to CategorySuggestion.Name.RentalIncome,
        )
    }
}
