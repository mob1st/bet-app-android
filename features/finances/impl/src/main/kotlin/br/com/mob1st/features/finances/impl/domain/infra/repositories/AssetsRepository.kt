package br.com.mob1st.features.finances.impl.domain.infra.repositories

import br.com.mob1st.core.kotlinx.structures.Uri
import kotlinx.coroutines.flow.Flow

interface AssetsRepository {
    fun getByTag(tag: String): Flow<List<Uri>>
}
