package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import kotlinx.coroutines.flow.Flow

internal interface CategoryRepository {
    fun getManuallyCreatedBy(
        type: CategoryType,
        isExpense: Boolean,
    ): Flow<List<Category>>

    suspend fun add(
        category: Category,
        linkedSuggestion: CategorySuggestion?,
    )

    suspend fun set(category: Category)

    suspend fun deleteRecurrence(
        category: Category,
        recurrenceIndex: Int,
    )

    suspend fun delete(category: Category)
}
