package br.com.mob1st.features.twocents.builder.impl.domain.usecases

import br.com.mob1st.core.database.RecurrenceType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class GetSuggestionsUseCase {
    operator fun get(recurrenceType: RecurrenceType): Flow<List<CategorySuggestion>> {
        return flowOf()
    }
}
