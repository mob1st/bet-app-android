package br.com.mob1st.features.finances.impl.data.repositories.suggestions

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import br.com.mob1st.features.finances.impl.SelectSuggestions
import br.com.mob1st.features.finances.impl.SuggestionsQueries
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

internal fun SuggestionsQueries.selectSuggestions(
    context: CoroutineContext,
    categoryType: CategoryType,
    isExpense: Boolean,
    mapper: (query: List<SelectSuggestions>) -> List<CategorySuggestion>,
): Flow<List<CategorySuggestion>> {
    return selectSuggestions(
        type = categoryType.asRow(),
        is_expense = isExpense,
    ).asFlow().mapToList(context).map(mapper)
}

private fun CategoryType.asRow(): String = when (this) {
    CategoryType.Fixed -> "fixed"
    CategoryType.Seasonal -> "seasonal"
    CategoryType.Variable -> "variable"
}
