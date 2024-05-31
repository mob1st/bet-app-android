package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

interface RecurrentCategoryRepository {
    suspend fun put(recurrences: List<RecurrentCategory>)
}
