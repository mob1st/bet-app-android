package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class SuggestionListPerStepTest {
    @ParameterizedTest
    @MethodSource("stepPerSuggestionSource")
    fun `GIVEN a step WHEN get THEN assert suggestions`(step: BuilderNextAction.Step, suggestions: List<String>) {
        val suggestionListPerStep = SuggestionListPerStep()
        val result = suggestionListPerStep[step]
        assert(result.containsAll(suggestions))
    }

    companion object {
        @JvmStatic
        fun stepPerSuggestionSource() = listOf(
            arguments(
                FixedExpensesStep,
                fixedExpensesSuggestions,
            ),
            arguments(
                VariableExpensesStep,
                variableExpensesSuggestions,
            ),
            arguments(
                SeasonalExpensesStep,
                seasonalExpensesSuggestions,
            ),
            arguments(
                FixedIncomesStep,
                fixedIncomesSuggestion,
            ),
        )

        private val fixedExpensesSuggestions = listOf(
            "finances_builder_suggestions_item_rent",
            "finances_builder_suggestions_item_mortgage",
            "finances_builder_suggestions_item_property_taxes",
            "finances_builder_suggestions_item_electricity",
            "finances_builder_suggestions_item_water",
            "finances_builder_suggestions_item_cleaning",
            "finances_builder_suggestions_item_health_insurance",
            "finances_builder_suggestions_item_health_care",
            "finances_builder_suggestions_item_pet_insurance",
            "finances_builder_suggestions_item_car_insurance",
            "finances_builder_suggestions_item_public_transport",
            "finances_builder_suggestions_item_home_insurance",
            "finances_builder_suggestions_item_bank_loan",
            "finances_builder_suggestions_item_internet_subscription",
            "finances_builder_suggestions_item_cell_phone_plan",
            "finances_builder_suggestions_item_cable_or_streaming_services",
            "finances_builder_suggestions_item_music_streaming_services",
            "finances_builder_suggestions_item_magazine_or_newspaper_subscriptions",
            "finances_builder_suggestions_item_gym",
            "finances_builder_suggestions_item_personal_education",
            "finances_builder_suggestions_item_children_school",
            "finances_builder_suggestions_item_childcare",
        )

        private val variableExpensesSuggestions = listOf(
            "finances_builder_suggestions_item_groceries",
            "finances_builder_suggestions_item_household_supplies",
            "finances_builder_suggestions_item_transportation_fuel",
            "finances_builder_suggestions_item_dining_out",
            "finances_builder_suggestions_item_food_delivery",
            "finances_builder_suggestions_item_coffee_snacks",
            "finances_builder_suggestions_item_weekday_lunch",
            "finances_builder_suggestions_item_public_transport_tickets",
            "finances_builder_suggestions_item_cinema",
            "finances_builder_suggestions_item_concerts",
            "finances_builder_suggestions_item_electronic_games",
            "finances_builder_suggestions_item_sports_tickets",
            "finances_builder_suggestions_item_bars",
            "finances_builder_suggestions_item_night_clubs",
            "finances_builder_suggestions_item_cigarettes",
            "finances_builder_suggestions_item_fitness_recreation",
        )

        private val seasonalExpensesSuggestions = listOf(
            "finances_builder_suggestions_item_holiday_gifts",
            "finances_builder_suggestions_item_dentist",
            "finances_builder_suggestions_item_vacation_travel",
            "finances_builder_suggestions_item_back_to_school_supplies",
            "finances_builder_suggestions_item_winter_clothing",
            "finances_builder_suggestions_item_garden_supplies",
            "finances_builder_suggestions_item_home_heating",
        )

        private val fixedIncomesSuggestion = listOf(
            "finances_builder_suggestions_item_salary",
            "finances_builder_suggestions_item_pension",
            "finances_builder_suggestions_item_rental_income",
        )
    }
}
