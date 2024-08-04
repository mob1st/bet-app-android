package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Completion
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderNavRoute.Step

/**
 * The router for the budget builder feature flow.
 * It intermediates the navigation events sent from ViewModel, typicically domain structures, into routes that can be
 * navigated by the UI.
 */
internal interface BuilderRouter {
    /**
     * Sends the given [action] to the router, returning the next route to navigate.
     * @param action The next action to be performed.
     * @return The next event navigation to be consumed by the UI.
     */
    fun to(action: BuilderNextAction): BuilderNavRoute

    /**
     * Receives the given [route] from the router, returning the next action to be performed.
     */
    fun from(route: Step): BuilderNextAction.Step

    companion object : BuilderRouter {
        override fun to(action: BuilderNextAction): BuilderNavRoute {
            return when (action) {
                BuilderNextAction.Complete -> Completion()
                FixedExpensesStep -> Step(Step.Id.FixedExpenses)
                FixedIncomesStep -> Step(Step.Id.FixedIncomes)
                SeasonalExpensesStep -> Step(Step.Id.SeasonalExpenses)
                VariableExpensesStep -> Step(Step.Id.VariableExpenses)
            }
        }

        override fun from(route: Step): BuilderNextAction.Step {
            return when (route.id) {
                Step.Id.FixedExpenses -> FixedExpensesStep
                Step.Id.VariableExpenses -> VariableExpensesStep
                Step.Id.SeasonalExpenses -> SeasonalExpensesStep
                Step.Id.FixedIncomes -> FixedIncomesStep
            }
        }
    }
}
