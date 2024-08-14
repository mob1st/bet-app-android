package br.com.mob1st.features.finances.impl.ui.builder.navigation

import br.com.mob1st.core.kotlinx.structures.biMapOf
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

    companion object {
        /**
         * Maps the step to the arguments and vice versa.
         */
        val map = biMapOf(
            FixedExpensesStepArgs to FixedExpensesStep,
            VariableExpensesStepArgs to VariableExpensesStep,
            SeasonalExpensesStepArgs to SeasonalExpensesStep,
            FixedIncomesStepArgs to FixedIncomesStep,
        )
    }
}
