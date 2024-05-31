package br.com.mob1st.features.dev.impl.domain

import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment

/**
 * The list of entries of the dev menu.
 *
 * It's a hardcoded list of the options we display in the dev menu feature.
 * Use the constructor with the [BackendEnvironment] to create a dev menu for the current environment.
 */
internal data class DevMenu(
    val backendEnvironment: BackendEnvironment,
    val entries: List<DevMenuEntry>,
)

/**
 * The possible entries of the dev menu.
 */
internal enum class DevMenuEntry(val isImplemented: Boolean) {
    Environment(false),
    Gallery(true),
    FeatureFlags(false),
    EntryPoint(false),
}
