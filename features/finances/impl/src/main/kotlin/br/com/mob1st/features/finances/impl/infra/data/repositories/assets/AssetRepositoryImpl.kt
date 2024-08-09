package br.com.mob1st.features.finances.impl.infra.data.repositories.assets

import br.com.mob1st.core.androidx.assets.AssetsGetter
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Asset
import br.com.mob1st.features.finances.impl.domain.infra.repositories.AssetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Default implementation of [AssetRepository].
 */
internal class AssetRepositoryImpl(
    private val assetsGetter: AssetsGetter,
    private val io: IoCoroutineDispatcher,
) : AssetRepository {
    override fun getByTag(tag: String): Flow<List<Asset>> {
        return flow {
            val file = checkNotNull(
                assetsGetter.get("icons/finances_builder_suggestions_item_bars.svg"),
            )
            val uri = Uri(file)
            val asset = Asset(uri)
            emit(listOf(asset))
        }.flowOn(io)
    }
}
