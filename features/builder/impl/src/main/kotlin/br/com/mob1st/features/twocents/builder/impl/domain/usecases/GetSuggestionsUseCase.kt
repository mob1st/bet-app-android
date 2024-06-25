package br.com.mob1st.features.twocents.builder.impl.domain.usecases

import br.com.mob1st.core.kotlinx.structures.RowId
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf

internal class GetSuggestionsUseCase {
    operator fun get(type: CategoryType): Flow<List<CategorySuggestion>> {
        return flowOf(suggestionsPerCategory[type]).filterNotNull()
    }

    companion object {
        private val suggestionsPerCategory = mapOf(
            CategoryType.Fixed to listOf(
                CategorySuggestion(
                    id = RowId(),
                    name = CategorySuggestion.Name.RENT,
                ),
            ),
            CategoryType.Variable to listOf(
                CategorySuggestion(
                    id = RowId(),
                    name = CategorySuggestion.Name.PARTY,
                ),
            ),
        )
    }
}
