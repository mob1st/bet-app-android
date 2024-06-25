package br.com.mob1st.features.finances.impl.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.data.morphisms.SuggestionDataMapper
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.repositories.SuggestionRepository
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import kotlinx.coroutines.flow.Flow

internal class SuggestionRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val db: TwoCentsDb,
    private val mapper: SuggestionDataMapper,
) : SuggestionRepository {
    private val queries by lazy { db.suggestionsQueries }

    override fun getByTypeAndIsExpense(
        categoryType: CategoryType,
        isExpense: Boolean,
    ): Flow<List<CategorySuggestion>> {
        return queries.selectSuggestions(
            context = io,
            categoryType = categoryType,
            isExpense = isExpense,
        ) { query -> mapper.map(categoryType, query) }
    }
}
