package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.repositories.SuggestionsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Concrete implementation of the [SuggestionsRepository] interface.
 * @property io The IO dispatcher.
 * @property db The SqlDelight database instance.
 * @property mapper The mapper for the suggestion data.
 */
internal class SuggestionsRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val db: TwoCentsDb,
    private val mapper: SelectSuggestionsMapper,
) : SuggestionsRepository {
    override fun getByStep(
        step: BuilderNextAction.Step,
    ): Flow<List<CategorySuggestion>> {
        return db.suggestionsQueries.selectSuggestions(
            context = io,
            step = step,
        ) { query -> mapper.map(query) }
    }
}
