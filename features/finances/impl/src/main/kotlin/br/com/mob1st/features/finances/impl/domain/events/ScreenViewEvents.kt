package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryDetailArgs

/**
 * Creates the screen event related to a builder step.
 * @param step the step to create the event for.
 * @return the screen view event.
 */
internal fun AnalyticsEvent.Companion.builderStepScreenView(
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
internal fun AnalyticsEvent.Companion.builderIntroScreenViewEvent() = screenView("builder_intro")

/**
 * Creates the screen event related to the category detail sheet
 * @param args the arguments to create the event for.
 * @return the screen view event.
 */
internal fun AnalyticsEvent.Companion.categoryScreenViewEvent(
    args: CategoryDetailArgs,
): AnalyticsEvent {
    val params = mutableMapOf<String, Any>()
    when (args.intent) {
        is CategoryDetailArgs.Intent.Create -> params["intent"] = "create"
        is CategoryDetailArgs.Intent.Edit -> params["intent"] = "edit"
    }
    params["name"] = args.name
    params["recurrenceType"] = args.recurrenceType.name
    params["isExpense"] = args.isExpense
    return screenView("category", params)
}
