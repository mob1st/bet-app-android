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
            val name = names[first.sug_name]
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

    companion object {
        private val names = mapOf(
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
            "gym" to CategorySuggestion.Name.Gym,
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
            "concerts" to CategorySuggestion.Name.Concerts,
            "electronic_games" to CategorySuggestion.Name.ElectronicGames,
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
