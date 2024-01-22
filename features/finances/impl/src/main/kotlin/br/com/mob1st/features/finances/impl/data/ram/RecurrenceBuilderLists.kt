package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Holds the lists of [RecurrentCategory] that are being built by the user for each step in the builder.
 * It uses a [PersistentList] to ensure immutability and allow edition.
 */
internal data class RecurrenceBuilderLists(
    val fixedExpensesList: PersistentList<RecurrentCategory> = persistentListOf(),
    val variableExpensesList: PersistentList<RecurrentCategory> = persistentListOf(),
    val seasonalExpensesList: PersistentList<RecurrentCategory> = persistentListOf(),
    val incomesList: PersistentList<RecurrentCategory> = persistentListOf(),
)
