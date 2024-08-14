package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
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

/**
 * Creates the screen event related to the category detail sheet
 * @param intent the intent to create the event for.
 * @return the screen view event.
 */
fun AnalyticsEvent.Companion.categoryScreenViewEvent(
    intent: GetCategoryIntent,
    recurrenceType: RecurrenceType,
    isExpense: Boolean,
): AnalyticsEvent {
    val params = mutableMapOf<String, Any>()
    when (intent) {
        is GetCategoryIntent.Create -> params["intent"] = "create"
        is GetCategoryIntent.Edit -> params["intent"] = "edit"
    }
    params["name"] = intent.name
    params["recurrenceType"] = recurrenceType.name
    params["isExpense"] = isExpense
    return screenView("category", params)
}
