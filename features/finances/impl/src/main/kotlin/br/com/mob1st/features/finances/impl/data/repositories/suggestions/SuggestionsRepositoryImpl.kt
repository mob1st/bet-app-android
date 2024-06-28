package br.com.mob1st.features.finances.impl.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.SuggestionsQueries
import br.com.mob1st.features.finances.impl.data.morphisms.SuggestionDataMapper
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.repositories.SuggestionsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Concrete implementation of the [SuggestionsRepository] interface.
 * @property io The IO dispatcher.
 * @property queries The queries for the suggestions.
 * @property mapper The mapper for the suggestion data.
 */
internal class SuggestionsRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val queries: SuggestionsQueries,
    private val mapper: SuggestionDataMapper,
) : SuggestionsRepository {
    override fun getByTypeAndIsExpense(
        step: BuilderNextAction.Step,
    ): Flow<List<CategorySuggestion>> {
        return queries.selectSuggestions(
            context = io,
            step = step,
        ) { query -> mapper.map(step.type, query) }
    }
}
