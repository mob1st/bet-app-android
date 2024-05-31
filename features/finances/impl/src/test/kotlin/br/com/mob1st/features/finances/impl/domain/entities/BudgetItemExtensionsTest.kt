package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BudgetItemExtensionsTest {
    @Test
    fun `GIVEN a list of items WHEN group THEN total expense should be the sum of all expenses only`() {
        val expected = Money(100)
        val group =
            listOf(
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.EXPENSE,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.INCOME,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.EXPENSE,
                ),
            ).toBudgetItemGroup()
        val actual = group.summaries.expenses
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a list of items WHEN group THEN total incomes be the sum of all incomes only`() {
        val expected = Money(100)
        val group =
            listOf(
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.EXPENSE,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.INCOME,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.INCOME,
                ),
            ).toBudgetItemGroup()
        val actual = group.summaries.incomes
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a list of more expenses than incomes WHEN group THEN balance should be negative`() {
        val expected = Money(-50)
        val group =
            listOf(
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.EXPENSE,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.INCOME,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.EXPENSE,
                ),
            ).toBudgetItemGroup()
        val actual = group.summaries.balance
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a list of more incomes than expenses WHEN group THEN balance should be positive`() {
        val expected = Money(50)
        val group =
            listOf(
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.EXPENSE,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.INCOME,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.INCOME,
                ),
            ).toBudgetItemGroup()
        val actual = group.summaries.balance
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a list of items WHEN group THEN the proportion of expense items should be calculated by the total of expenses`() {
        val expected = 30
        val group =
            listOf(
                SomeBudgetItem(
                    amount = Money(60),
                    type = BudgetItem.Type.EXPENSE,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.INCOME,
                ),
                SomeBudgetItem(
                    amount = Money(140),
                    type = BudgetItem.Type.EXPENSE,
                ),
            ).toBudgetItemGroup()
        val actual = group.items.first { it.item.type == BudgetItem.Type.EXPENSE }.proportion
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a list of items WHEN group THEN the proportion of income items should be calculated by the total of incomes`() {
        val expected = 100
        val group =
            listOf(
                SomeBudgetItem(
                    amount = Money(60),
                    type = BudgetItem.Type.EXPENSE,
                ),
                SomeBudgetItem(
                    amount = Money(50),
                    type = BudgetItem.Type.INCOME,
                ),
                SomeBudgetItem(
                    amount = Money(140),
                    type = BudgetItem.Type.EXPENSE,
                ),
            ).toBudgetItemGroup()
        val actual = group.items.first { it.item.type == BudgetItem.Type.INCOME }.proportion
        assertEquals(expected, actual)
    }

    private data class SomeBudgetItem(
        override val amount: Money,
        override val type: BudgetItem.Type,
    ) : BudgetItem {
        override val description: String? = null
    }
}
