package br.com.mob1st.features.finances.impl.data.repositories

import br.com.mob1st.features.finances.impl.domain.repositories.RecurrentCategoryRepository
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory

class RecurrentCategoryRepositoryImpl : RecurrentCategoryRepository {
    override suspend fun put(recurrences: List<RecurrentCategory>) {
        TODO("Not yet implemented")
    }
}
