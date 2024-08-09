package br.com.mob1st.features.finances.impl.domain.infra.repositories

import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Asset
import kotlinx.coroutines.flow.Flow

/**
 * Provide access to the assets of the application.
 */
interface AssetRepository {
    /**
     * Get a list of assets that may be related for a given [tag].
     * The list of assets is sorted by the relevance relation it has with the given [tag], where
     * the first element is the most relevant and the last element is the least relevant.
     * @param tag The tag to search for.
     * @return A [Flow] of [List] of [Uri] that may be related to the given [tag]. The list is never empty
     */
    fun getByTag(tag: String): Flow<List<Asset>>
}
