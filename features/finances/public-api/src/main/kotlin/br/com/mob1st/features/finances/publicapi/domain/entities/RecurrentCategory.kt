package br.com.mob1st.features.finances.publicapi.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uuid

/**
 * A type of transaction that happens recurrently, every month, week or year.
 */
data class RecurrentCategory(
    override val id: Uuid = Uuid(),
    override val description: String? = null,
    override val amount: Money = Money.Zero,
    override val type: BudgetItem.Type,
    val recurrence: Recurrence,
) : Identifiable<Uuid>, BudgetItem
