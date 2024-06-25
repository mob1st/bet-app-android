package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import kotlinx.coroutines.flow.Flow

internal interface SuggestionRepository {
    fun getByTypeAndIsExpense(
        categoryType: CategoryType,
        isExpense: Boolean,
    ): Flow<List<CategorySuggestion>>
}
