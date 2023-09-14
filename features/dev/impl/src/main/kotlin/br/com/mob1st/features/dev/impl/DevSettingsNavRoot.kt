package br.com.mob1st.features.dev.impl

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.core.navigation.NavRoot
import br.com.mob1st.features.dev.impl.presentation.menu.DevMenuPage
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget

object DevSettingsNavRoot : NavRoot {
    context(NavGraphBuilder) override fun graph(navController: NavController) {
        navigation(
            startDestination = DevSettingsNavTarget.DevMenu.screenName,
            route = DevSettingsNavTarget.ROUTE
        ) {
            composable(DevSettingsNavTarget.DevMenu.screenName) {
                DevMenuPage()
            }
            composable(DevSettingsNavTarget.BackendEnvironment.screenName) {
                // TODO implement backend environment page
            }
        }
    }
}
