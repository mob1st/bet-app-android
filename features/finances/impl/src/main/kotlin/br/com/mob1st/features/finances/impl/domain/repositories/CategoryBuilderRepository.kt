package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategoryBuilder
import kotlinx.coroutines.flow.Flow

internal interface CategoryBuilderRepository {
    fun getBy(step: BuilderNextAction.Step): Flow<CategoryBuilder>

    suspend fun complete()
}
