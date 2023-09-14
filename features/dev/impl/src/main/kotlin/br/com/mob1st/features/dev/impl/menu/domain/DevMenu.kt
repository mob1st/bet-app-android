package br.com.mob1st.features.dev.impl.menu.domain

import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment

/**
 * The list of entries of the dev menu.
 *
 * It's a hardcoded list of the options we display in the dev menu feature.
 * Use the constructor with the [BackendEnvironment] to create a dev menu for the current environment.
 */
internal data class DevMenu(
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

/**
 * The possible entries of the dev menu.
 */
internal sealed class DevMenuEntry {

    data class Environment(val type: BackendEnvironment) : DevMenuEntry()
    data object Gallery : DevMenuEntry()

    data object FeatureFlags : DevMenuEntry()

    data object EntryPoint : DevMenuEntry()
}
