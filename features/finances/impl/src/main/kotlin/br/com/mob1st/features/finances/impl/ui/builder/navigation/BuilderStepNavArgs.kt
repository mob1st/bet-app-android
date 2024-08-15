package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep

/**
 * Defines which step will be navigated to based on the arguments.
 */
enum class BuilderStepNavArgs {
    FixedExpensesStepArgs,
    VariableExpensesStepArgs,
    SeasonalExpensesStepArgs,
    FixedIncomesStepArgs,
    ;

    fun toStep() = when (this) {
        FixedExpensesStepArgs -> FixedExpensesStep
        VariableExpensesStepArgs -> VariableExpensesStep
        SeasonalExpensesStepArgs -> SeasonalExpensesStep
        FixedIncomesStepArgs -> FixedIncomesStep
    }

    companion object {
        fun fromStep(step: BudgetBuilderAction.Step) = when (step) {
            FixedExpensesStep -> FixedExpensesStepArgs
            FixedIncomesStep -> FixedIncomesStepArgs
            SeasonalExpensesStep -> SeasonalExpensesStepArgs
            VariableExpensesStep -> VariableExpensesStepArgs
        }
    }
}
