package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroConsumables
import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroNextStepNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepCategoryDetailNavEvent
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepConsumables
import br.com.mob1st.features.finances.impl.ui.builder.steps.BuilderStepNextNavEvent
import br.com.mob1st.features.finances.impl.ui.category.components.sheet.Args

/**
 * The router for the budget builder feature flow.
 * It intermediates the navigation events sent from ViewModels and maps it to routes that can be navigated by the UI.
 * Also, it has capabilities to extract the routes parameters to domain specific data, that can be used as arguments by
 * screens.
 */
internal object BuilderRouter {
    /**
     * Navigates from the intro screen to a [BuilderNavRoute].
     * @param navEvent The navigation event to be consumed.
     * @return The next event navigation to be consumed by the UI.
     */
    fun route(navEvent: BuilderIntroConsumables.NavEvent): BuilderNavRoute = when (navEvent) {
        is BuilderIntroNextStepNavEvent -> route(navEvent.step)
    }

    /**
     * Navigates from the step screen to a [BuilderNavRoute].
     * @param navEvent The navigation event to be consumed.
     * @return The next event navigation to be consumed by the UI.
     */
    fun route(navEvent: BuilderStepConsumables.NavEvent): BuilderNavRoute = when (navEvent) {
        is BuilderStepNextNavEvent -> route(navEvent.next)
        is BuilderStepCategoryDetailNavEvent -> BuilderNavRoute.CategoryDetail(
            args = when (navEvent.intent) {
                is GetCategoryIntent.Create -> Args(name = navEvent.intent.name)
                is GetCategoryIntent.Edit -> Args(id = navEvent.intent.id.value)
            },
        )
    }

    /**
     * Receives the given [route] from the router, returning the next action to be performed.
     */
    fun receive(route: BuilderNavRoute.Step): BudgetBuilderAction.Step = when (route.id) {
        BuilderNavRoute.Step.Id.FixedExpenses -> FixedExpensesStep
        BuilderNavRoute.Step.Id.VariableExpenses -> VariableExpensesStep
        BuilderNavRoute.Step.Id.SeasonalExpenses -> SeasonalExpensesStep
        BuilderNavRoute.Step.Id.FixedIncomes -> FixedIncomesStep
    }

    private fun route(action: BudgetBuilderAction): BuilderNavRoute {
        return when (action) {
            BudgetBuilderAction.Complete -> BuilderNavRoute.Completion()
            FixedExpensesStep -> BuilderNavRoute.Step(BuilderNavRoute.Step.Id.FixedExpenses)
            FixedIncomesStep -> BuilderNavRoute.Step(BuilderNavRoute.Step.Id.FixedIncomes)
            SeasonalExpensesStep -> BuilderNavRoute.Step(BuilderNavRoute.Step.Id.SeasonalExpenses)
            VariableExpensesStep -> BuilderNavRoute.Step(BuilderNavRoute.Step.Id.VariableExpenses)
        }
    }
}
