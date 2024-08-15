package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNavArgs
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailArgs

/**
 * Creates the screen event related to a builder step.
 * @param step the step to create the event for.
 * @return the screen view event.
 */
internal fun AnalyticsEvent.Companion.builderStepScreenView(
    args: BuilderStepNavArgs,
) = when (args) {
    BuilderStepNavArgs.FixedExpensesStepArgs -> screenView("builder_fixed_expenses")
    BuilderStepNavArgs.VariableExpensesStepArgs -> screenView("builder_variable_expenses")
    BuilderStepNavArgs.FixedIncomesStepArgs -> screenView("builder_fixed_incomes")
    BuilderStepNavArgs.SeasonalExpensesStepArgs -> screenView("builder_seasonal_expenses")
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
