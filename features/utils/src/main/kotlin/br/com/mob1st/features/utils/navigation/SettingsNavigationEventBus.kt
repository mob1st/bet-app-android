package br.com.mob1st.features.utils.navigation

import br.com.mob1st.core.kotlinx.coroutines.BroadcastEventBus

object SettingsNavigationEventBus : BroadcastEventBus<SettingsNavigationEventBus.Target>() {

    sealed class Target {
        data object Connection : Target()
        data object Permissions : Target()
    }
}
