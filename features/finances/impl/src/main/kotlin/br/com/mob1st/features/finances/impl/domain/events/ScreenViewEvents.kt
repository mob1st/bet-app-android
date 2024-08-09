package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep

/**
 * Creates the screen event related to a builder step.
 * @param step the step to create the event for.
 * @return the screen view event.
 */
fun AnalyticsEvent.Companion.builderStepScreenView(
    step: BudgetBuilderAction.Step,
) = when (step) {
    FixedExpensesStep -> screenView("builder_fixed_expenses")
    VariableExpensesStep -> screenView("builder_variable_expenses")
    FixedIncomesStep -> screenView("builder_fixed_incomes")
    SeasonalExpensesStep -> screenView("builder_seasonal_expenses")
}

/**
 * Creates the screen event related to the builder intro screen.
 * @return the screen view event.
 */
fun AnalyticsEvent.Companion.builderIntroScreenViewEvent() = screenView("builder_intro")

fun AnalyticsEvent.Companion.categoryScreenViewEvent(
    intent: GetCategoryIntent,
): AnalyticsEvent {
    val params = when (intent) {
        is GetCategoryIntent.Create -> mapOf(
            "intent" to "create",
            "isSuggested" to false,
            "name" to intent.name,
            "isExpense" to intent.isExpense,
            "recurrenceType" to intent.type,
        )

        is GetCategoryIntent.Edit -> mapOf(
            "intent" to "edit",
            "id" to intent.id,
            "isSuggested" to false,
        )
    }
    return screenView("category", params)
}
