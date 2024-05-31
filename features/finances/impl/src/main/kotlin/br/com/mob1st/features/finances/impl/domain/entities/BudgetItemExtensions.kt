package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItemGroup

/**
 * Transforms a list of [BudgetItem] into a [BudgetItemGroup].
 * @param T The type of [BudgetItem] in the list.
 * @return A [BudgetItemGroup] with the list of items and the total amount.
 */
internal fun <T : BudgetItem> List<T>.toBudgetItemGroup(): BudgetItemGroup<T> {
    val summaries = calculateSummaries()
    return BudgetItemGroup(
        items = map { item -> item.proportionOf(summaries) },
        summaries = summaries,
    )
}

private fun List<BudgetItem>.calculateSummaries(): BudgetItemGroup.Summaries {
    var incomesTotal = Money.Zero
    var expensesTotal = Money.Zero
    forEach {
        if (it.type == BudgetItem.Type.EXPENSE) {
            expensesTotal += it.amount
        } else {
            incomesTotal += it.amount
        }
    }
    return BudgetItemGroup.Summaries(
        incomes = incomesTotal,
        expenses = expensesTotal,
        balance = incomesTotal - expensesTotal,
    )
}

private fun <T : BudgetItem> T.proportionOf(summaries: BudgetItemGroup.Summaries): BudgetItemGroup.ProportionalItem<T> {
    val total =
        when (type) {
            BudgetItem.Type.EXPENSE -> summaries.expenses
            BudgetItem.Type.INCOME -> summaries.incomes
        }
    return BudgetItemGroup.ProportionalItem(
        item = this,
        proportion = ((amount.cents.toDouble() / total.cents.toDouble()) * 100).toInt(),
    )
}
