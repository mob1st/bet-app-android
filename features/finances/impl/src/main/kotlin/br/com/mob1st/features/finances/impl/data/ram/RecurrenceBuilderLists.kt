package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Holds the lists of [RecurrentCategory] that are being built by the user for each step in the builder.
 * It uses a [PersistentList] to ensure immutability and allow edition.
 */
internal data class RecurrenceBuilderLists(
    val fixedExpensesList: PersistentList<RecurrentCategory.Fixed> = persistentListOf(),
    val variableExpensesList: PersistentList<RecurrentCategory.Variable> = persistentListOf(),
    val seasonalExpensesList: PersistentList<RecurrentCategory.Seasonal> = persistentListOf(),
    val incomesList: PersistentList<RecurrentCategory> = persistentListOf(),
)
