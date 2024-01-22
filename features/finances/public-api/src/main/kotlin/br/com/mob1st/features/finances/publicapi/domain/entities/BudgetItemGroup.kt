package br.com.mob1st.features.finances.publicapi.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money

/**
 * Group of recurrent categories
 * @property items list of recurrent categories
 * @param T type of recurrent category
 */
data class BudgetItemGroup<T : BudgetItem>(
    /**
     * List of recurrent categories
     */
    val items: List<ProportionalItem<T>>,

    /**
     * The total amount of all recurrent categories
     */
    val summaries: Summaries,
) {

    /**
     * The total amount of all recurrent categories, separated by [BudgetItem.Type]
     */
    data class Summaries(
        val incomes: Money,
        val expenses: Money,
        val balance: Money,
    )

    /**
     * A proportion of an item in relation to the total amount of all recurrent categories.
     * If the [item] type is [BudgetItem.Type.EXPENSE], the proportion is calculated in relation to the
     * [Summaries.expenses]. Otherwise, it is calculated in relation to the [Summaries.incomes].
     * @property item the budget item
     * @property proportion the proportion of the item in relation to the total amount
     */
    data class ProportionalItem<T : BudgetItem>(
        val item: T,
        val proportion: Int,
    )
}
