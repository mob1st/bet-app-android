package br.com.mob1st.features.dev.publicapi.presentation

import kotlinx.serialization.Serializable

/**
 * Dev settings navigation targets.
 */
sealed interface DevSettingsNavTarget {
    /**
     * Dev menu screen. The first screen of the dev settings.
     */
    @Serializable
    data object DevMenu : DevSettingsNavTarget

    /**
     * Backend environment screen.
     */
    @Serializable
    data object BackendEnvironment : DevSettingsNavTarget

    /**
     * Feature flags screen.
     */
    @Serializable
    data object FeatureFlags : DevSettingsNavTarget

    /**
     * Entry points to specific features screen in the app.
     */
    @Serializable
    data object EntryPoints : DevSettingsNavTarget

    interface Nested : DevSettingsNavTarget

    @Serializable
    object Route
}
