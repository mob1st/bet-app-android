package br.com.mob1st.features.finances.impl.ui.builder.navigation

import arrow.optics.PIso
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep

/**
 * Maps builder next actions into the proper builder step route.
 * In case the given arguments are a step into the builder, it nests the [builderStepIso] implementation.
 * @see builderStepIso
 */
internal val builderNextActionIso = PIso(
    get = { args: BuilderNextAction ->
        when (args) {
            is BuilderNextAction.Step -> builderStepIso.get(args)
            BuilderNextAction.Complete -> BuilderRoute.Completion
        }
    },
    reverseGet = { route: BuilderRoute ->
        when (route) {
            is BuilderRoute.Step -> builderStepIso.reverseGet(route)
            BuilderRoute.Completion -> BuilderNextAction.Complete
            else -> error("Invalid route for $route")
        }
    },
)

/**
 * Maps builder steps into the proper builder step route
 */
internal val builderStepIso = PIso(
    get = { args: BuilderNextAction.Step ->
        when (args) {
            FixedExpensesStep -> BuilderRoute.Step(BuilderRoute.Step.Type.FixedExpenses)
            FixedIncomesStep -> BuilderRoute.Step(BuilderRoute.Step.Type.FixedIncomes)
            VariableExpensesStep -> BuilderRoute.Step(BuilderRoute.Step.Type.VariableExpenses)
            SeasonalExpensesStep -> BuilderRoute.Step(BuilderRoute.Step.Type.SeasonalExpenses)
        }
    },
    reverseGet = { route: BuilderRoute.Step ->
        when (route.type) {
            BuilderRoute.Step.Type.FixedExpenses -> FixedExpensesStep
            BuilderRoute.Step.Type.FixedIncomes -> FixedIncomesStep
            BuilderRoute.Step.Type.VariableExpenses -> VariableExpensesStep
            BuilderRoute.Step.Type.SeasonalExpenses -> SeasonalExpensesStep
        }
    },
)
