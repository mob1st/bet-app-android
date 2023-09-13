package br.com.mob1st.features.utils.navigation

import br.com.mob1st.core.kotlinx.coroutines.BroadcastEventBus

/**
 * An event bus that triggers commands to navigate to the system settings screens.
 * If consumed by a Single Activity that handles a feature flow, it can be used to apply common navigations to out of
 * the app
 */
@JvmInline
value class SettingsNavigationEventBus(
    private val delegated: BroadcastEventBus<Target> = BroadcastEventBus(),
) : BroadcastEventBus<SettingsNavigationEventBus.Target> by delegated {

    /**
     * The possible targets to the settings screens.
     *
     * Add more variants on demand. If parameters are required, use a Parcelable data class instead of objects
     */
    sealed class Target {

        /**
         * Navigates to the internet connection settings screen
         */
        data object Connection : Target()

        /**
         * Navigates to the app permissions screen
         */
        data object Permissions : Target()
    }
}
