package br.com.mob1st.features.dev.impl.menu.domain

import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment

data class DevMenu(
    val entries: List<DevMenuEntry>,
) {
    constructor(currentEnv: BackendEnvironment) : this(
        entries = listOf(
            DevMenuEntry.Environment(currentEnv),
            DevMenuEntry.FeatureFlags,
            DevMenuEntry.Gallery,
            DevMenuEntry.EntryPoint
        )
    )
    fun isAllowed(position: Int) = entries[position] == DevMenuEntry.Gallery
}
sealed class DevMenuEntry {

    data class Environment(val type: BackendEnvironment) : DevMenuEntry()
    data object Gallery : DevMenuEntry()

    data object FeatureFlags : DevMenuEntry()

    data object EntryPoint : DevMenuEntry()
}
