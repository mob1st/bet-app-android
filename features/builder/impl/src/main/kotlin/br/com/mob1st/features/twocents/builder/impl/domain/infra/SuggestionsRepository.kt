package br.com.mob1st.features.twocents.builder.impl.domain.infra

import br.com.mob1st.core.database.RecurrenceType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.coroutines.flow.Flow

internal interface SuggestionsRepository {
    operator fun get(recurrenceType: RecurrenceType): Flow<List<CategorySuggestion>>
}
