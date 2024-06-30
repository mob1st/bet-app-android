package br.com.mob1st.features.finances.impl.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.impl.Category_view
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.data.repositories.categories.SelectCategoryViewsMapper
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import timber.log.Timber

/**
 * Maps a list of [SelectSuggestions] provided by the database to a list of [CategorySuggestion] domain entity.
 * It uses the [SelectCategoryViewsMapper] to map the linked categories. For that it's important to use the same
 * contract in the query used to get the category data.
 * @property listCategoryViewMapper The mapper for the category data.
 */
internal class SelectSuggestionsMapper(
    private val listCategoryViewMapper: SelectCategoryViewsMapper,
) {
    /**
     * Maps the given [query] to a list of [CategorySuggestion] domain entities.
     * @param type The type of the categories to be mapped.
     * @param query The list of [SelectSuggestions] to be mapped.
     * @return The list of [CategorySuggestion] domain entities.
     * @see SelectCategoryViewsMapper
     */
    fun map(
        type: CategoryType,
        query: List<SelectSuggestions>,
    ): List<CategorySuggestion> {
        return query.groupBy { it.sug_id }.mapNotNull { entry ->
            val first = entry.value.first()
            val name = first.sug_name.asSuggestionName()
            if (name == null) {
                Timber.w("Unknown suggestion name: $this. Suggestion will be discarded from the list.")
                return@mapNotNull null
            }
            val linkedCategory = runCatching {
                listCategoryViewMapper.map(type, entry)
            }.onFailure {
                Timber.e(it, "Error mapping category view ${first.cat_id} from suggestion ${first.sug_id}.")
                return@mapNotNull null
            }.getOrNull()
            CategorySuggestion(
                id = RowId(first.sug_id),
                name = name,
                linkedCategory = linkedCategory,
            )
        }
    }
}

@Suppress("CyclomaticComplexMethod")
private fun String.asSuggestionName(): CategorySuggestion.Name? {
    return when (this) {
        "rent_or_mortgage" -> CategorySuggestion.Name.RentOrMortgage
        "property_taxes" -> CategorySuggestion.Name.PropertyTaxes
        "health_insurance" -> CategorySuggestion.Name.HealthInsurance
        "car_insurance" -> CategorySuggestion.Name.CarInsurance
        "public_transport" -> CategorySuggestion.Name.PublicTransport
        "home_insurance" -> CategorySuggestion.Name.HomeInsurance
        "loan_payments" -> CategorySuggestion.Name.LoanPayments
        "internet_subscription" -> CategorySuggestion.Name.InternetSubscription
        "cell_phone_plan" -> CategorySuggestion.Name.CellPhonePlan
        "cable_or_streaming_services" -> CategorySuggestion.Name.CableOrStreamingServices
        "music_streaming_services" -> CategorySuggestion.Name.MusicStreamingServices
        "magazine_or_newspaper_subscriptions" -> CategorySuggestion.Name.MagazineOrNewspaperSubscriptions
        "gym" -> CategorySuggestion.Name.Gym
        "association_fees" -> CategorySuggestion.Name.AssociationFees
        "private_retirement_plans" -> CategorySuggestion.Name.PrivateRetirementPlans
        "personal_education" -> CategorySuggestion.Name.PersonalEducation
        "children_school" -> CategorySuggestion.Name.ChildrenSchool
        "childcare" -> CategorySuggestion.Name.Childcare
        "groceries" -> CategorySuggestion.Name.Groceries
        "dining_out" -> CategorySuggestion.Name.DiningOut
        "food_delivery" -> CategorySuggestion.Name.FoodDelivery
        "weekday_lunch" -> CategorySuggestion.Name.WeekdayLunch
        "coffee_snacks" -> CategorySuggestion.Name.CoffeeSnacks
        "transportation_fuel" -> CategorySuggestion.Name.TransportationFuel
        "public_transport_tickets" -> CategorySuggestion.Name.PublicTransportTickets
        "cinema" -> CategorySuggestion.Name.Cinema
        "sports_tickets" -> CategorySuggestion.Name.SportsTickets
        "electronic_games" -> CategorySuggestion.Name.ElectronicGames
        "bars" -> CategorySuggestion.Name.Bars
        "night_clubs" -> CategorySuggestion.Name.NightClubs
        "household_supplies" -> CategorySuggestion.Name.HouseholdSupplies
        "fitness_recreation" -> CategorySuggestion.Name.FitnessRecreation
        "holiday_gifts" -> CategorySuggestion.Name.HolidayGifts
        "vacation_travel" -> CategorySuggestion.Name.VacationTravel
        "back_to_school_supplies" -> CategorySuggestion.Name.BackToSchoolSupplies
        "winter_clothing" -> CategorySuggestion.Name.WinterClothing
        "summer_activities" -> CategorySuggestion.Name.SummerActivities
        "garden_supplies" -> CategorySuggestion.Name.GardenSupplies
        "home_heating" -> CategorySuggestion.Name.HomeHeating
        "holiday_decorations" -> CategorySuggestion.Name.HolidayDecorations
        "tax_preparation_fees" -> CategorySuggestion.Name.TaxPreparationFees
        "spring_cleaning" -> CategorySuggestion.Name.SpringCleaning
        "salary" -> CategorySuggestion.Name.Salary
        "pension" -> CategorySuggestion.Name.Pension
        "rental_income" -> CategorySuggestion.Name.RentalIncome
        else -> null
    }
}

private fun SelectCategoryViewsMapper.map(
    type: CategoryType,
    entry: Map.Entry<Long, List<SelectSuggestions>>,
): Category? {
    val views = entry.value.mapNotNull {
        it.asCategoryView()
    }
    val categories = map(type, views)
    if (categories.size > 1) {
        // the relation contract between suggestion and category should be 1:1
        // it can be ignored, but it's important to log it.
        Timber.w("More than one category was mapped for a single suggestion.")
    }
    return categories.firstOrNull()
}

private fun SelectSuggestions.asCategoryView(): Category_view? {
    return cat_id?.let { id ->
        check(cat_linked_suggestion_id == sug_id) {
            "The category's linked suggestion id must be the same as the suggestion id."
        }
        // manually creates the SqlDelight generated class to reuse the SelectCategoryViewsMapper method
        Category_view(
            cat_id = id,
            cat_name = checkNotNull(cat_name),
            cat_is_expense = checkNotNull(cat_is_expense),
            cat_amount = checkNotNull(cat_amount),
            cat_created_at = cat_created_at.orEmpty(),
            cat_linked_suggestion_id = cat_linked_suggestion_id,
            frc_day_of_month = frc_day_of_month,
            vrc_day_of_week = vrc_day_of_week,
            src_day = src_day,
            src_month = src_month,
        )
    }
}
