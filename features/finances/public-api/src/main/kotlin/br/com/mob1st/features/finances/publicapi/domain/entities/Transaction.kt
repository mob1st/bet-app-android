package br.com.mob1st.features.finances.publicapi.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uuid
import br.com.mob1st.core.kotlinx.structures.Writable
import br.com.mob1st.core.kotlinx.structures.WriteRegistry
import kotlinx.datetime.Instant

/**
 * A transaction added in the user's budget.
 * @property instant the moment when the transaction happened
 * @property recurrentCategory the associated recurrent category, if any
 */
data class Transaction(
    override val amount: Money,
    override val type: BudgetItem.Type,
    val instant: Instant,
    override val id: Uuid = Uuid(),
    override val description: String? = null,
    override val writeRegistry: WriteRegistry = WriteRegistry(),
    val recurrentCategory: RecurrentCategory? = null,
) : BudgetItem, Identifiable<Uuid>, Writable
