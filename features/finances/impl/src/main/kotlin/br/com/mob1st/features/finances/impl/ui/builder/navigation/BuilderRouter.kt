package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute.Step.Type.FixedExpenses
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute.Step.Type.FixedIncomes
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute.Step.Type.SeasonalExpenses
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute.Step.Type.VariableExpenses

/**
 * The router for the budget builder feature flow.
 * It intermediates the navigation events sent from ViewModel, typicically domain structures, into routes that can be
 * navigated by the UI.
 */
interface BuilderRouter {
    /**
     * Sends the given [action] to the router, returning the next route to navigate.
     * @param action The next action to be performed.
     * @return The next route to navigate.
     */
    fun send(action: BuilderNextAction): BuilderRoute

    /**
     * Receives the given [route] from the router, returning the next action to be performed.
     */
    fun receive(route: BuilderRoute.Step): BuilderNextAction.Step

    companion object : BuilderRouter {
        override fun send(action: BuilderNextAction): BuilderRoute {
            return when (action) {
                FixedExpensesStep -> BuilderRoute.Step(FixedExpenses)
                FixedIncomesStep -> BuilderRoute.Step(FixedIncomes)
                SeasonalExpensesStep -> BuilderRoute.Step(SeasonalExpenses)
                VariableExpensesStep -> BuilderRoute.Step(VariableExpenses)
                BuilderNextAction.Complete -> BuilderRoute.Completion
            }
        }

        override fun receive(route: BuilderRoute.Step): BuilderNextAction.Step {
            return when (route.type) {
                FixedExpenses -> FixedExpensesStep
                FixedIncomes -> FixedIncomesStep
                SeasonalExpenses -> SeasonalExpensesStep
                VariableExpenses -> VariableExpensesStep
            }
        }
    }
}
