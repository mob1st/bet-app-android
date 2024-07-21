package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep

/**
 * Returns the arguments used in the selectCategoriesByRecurrenceType query based on the given [step].
 */
internal data class SelectCategoriesByStepArgs(
    val isExpense: Boolean,
    val recurrenceTypeDescription: String,
) {
    companion object {
        fun from(step: BuilderNextAction.Step): SelectCategoriesByStepArgs {
            val isExpense = step != FixedIncomesStep
            val recurrenceTypeDescription = when (step) {
                FixedIncomesStep,
                FixedExpensesStep,
                -> RecurrenceType.Fixed.value

                VariableExpensesStep -> RecurrenceType.Variable.value
                is SeasonalExpensesStep -> RecurrenceType.Seasonal.value
            }
            return SelectCategoriesByStepArgs(
                isExpense = isExpense,
                recurrenceTypeDescription = recurrenceTypeDescription,
            )
        }
    }
}
