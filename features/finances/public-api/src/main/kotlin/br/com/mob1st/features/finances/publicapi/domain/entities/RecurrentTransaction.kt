package br.com.mob1st.features.finances.publicapi.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money

/**
 * A transaction may happen in the future and is related to a [RecurrentCategory]
 */
data class RecurrentTransaction(
    override val type: BudgetItem.Type,
    override val amount: Money = Money.Zero,
    override val description: String? = null,
) : BudgetItem
