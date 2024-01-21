package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem

/**
 * Sums a list of [BudgetItem] values.
 * @return The sum of all [BudgetItem] values.
 */
fun List<BudgetItem>.sum(): Money = Money(sumOf { it.amount.cents })

/**
 * Calculates the proportion of this [BudgetItem] in relation to the [total].
 */
fun BudgetItem.proportionOf(total: Money): Int {
    return (amount / total).cents * 100
}
