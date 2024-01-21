package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategoryGroup

/**
 * Group of recurrent categories
 */
private data class RecurrentCategoryGroupImpl<T : RecurrentCategory>(
    override val items: List<T>,
) : RecurrentCategoryGroup<T> {

    override val total: Money = items.map { it.recurrentTransaction }.sum()
    override fun proportionOf(position: Int): Int {
        return items[position].recurrentTransaction.proportionOf(total)
    }
}

/**
 * Create a [RecurrentCategoryGroup] from a list of recurrent categories
 */
internal fun <T : RecurrentCategory> RecurrentCategoryGroup(items: List<T>): RecurrentCategoryGroup<T> =
    RecurrentCategoryGroupImpl(items)
