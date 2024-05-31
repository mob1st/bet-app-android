package br.com.mob1st.features.dev.impl.domain.repositories

import br.com.mob1st.features.dev.impl.domain.DevMenu

internal interface DevMenuRepository {
    suspend fun getBy(): List<DevMenu>

    suspend fun get(position: Int): DevMenu
}
