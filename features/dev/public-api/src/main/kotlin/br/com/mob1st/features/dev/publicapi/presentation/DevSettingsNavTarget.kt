package br.com.mob1st.features.dev.publicapi.presentation

import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

/**
 * Dev settings navigation targets.
 */
sealed class DevSettingsNavTarget : NavTarget() {

    /**
     * Dev menu screen. The first screen of the dev settings.
     */
    data object DevMenu : DevSettingsNavTarget() {
        override val screenName: String = "devMenu"
    }

    /**
     * Gallery screen.
     */
    data object Gallery : DevSettingsNavTarget() {
        override val screenName: String = "gallery"
    }

    /**
     * Backend environment screen.
     */
    data object BackendEnvironment : DevSettingsNavTarget() {
        override val screenName: String = "backendEnvironment"
    }

    /**
     * Feature flags screen.
     */
    data object FeatureFlags : DevSettingsNavTarget() {
        override val screenName: String = "featureFlags"
    }

    data object EntryPoints : DevSettingsNavTarget() {
        override val screenName: String = "entryPoints"
    }

    companion object {
        /**
         * Route for the dev settings navigation.
         */
        const val ROUTE = "br.com.mob1st.features.dev"
    }
}
