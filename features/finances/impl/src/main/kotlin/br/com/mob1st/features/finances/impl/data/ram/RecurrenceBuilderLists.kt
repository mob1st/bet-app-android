package br.com.mob1st.features.finances.impl.data.ram

import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

internal data class RecurrenceBuilderLists(
    val fixedExpensesList: PersistentList<RecurrentCategory.Fixed> = persistentListOf(),
    val variableExpensesList: PersistentList<RecurrentCategory.Variable> = persistentListOf(),
    val seasonalExpensesList: PersistentList<RecurrentCategory.Seasonal> = persistentListOf(),
    val incomesList: PersistentList<RecurrentCategory> = persistentListOf(),
)
