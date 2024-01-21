package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import kotlinx.collections.immutable.PersistentList

internal data class RecurrenceBuilder(
    val fixedExpensesStep: Step<RecurrentCategory.Fixed>,
    val variableExpensesStep: Step<RecurrentCategory.Variable>,
    val seasonalExpensesStep: Step<RecurrentCategory.Seasonal>,
    val incomesStep: Step<RecurrentCategory>,
) {
    /**
     * The list of all recurrent categories for the step.
     * @property list all recurrent categories for this step
     * @property isCompleted true if the step is already completed, otherwise false
     */
    data class Step<T : RecurrentCategory>(
        val list: PersistentList<T>,
        val isCompleted: Boolean,
    )
}

/**
 * Complete the step of fixed expenses.
 */
internal fun RecurrenceBuilder.completeFixedExpensesStep(): RecurrenceBuilder {
    return copy(
        fixedExpensesStep = fixedExpensesStep.copy(
            isCompleted = true
        )
    )
}

/**
 * Get all recurrent categories where a value was filled.
 */
internal fun <T : RecurrentCategory> RecurrenceBuilder.Step<T>.getFilled(): List<T> {
    return list.filter {
        it.recurrentTransaction.amount > Money.Zero
    }
}
