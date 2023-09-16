package br.com.mob1st.features.dev.impl

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.core.design.atoms.properties.navigations.navigate
import br.com.mob1st.features.dev.impl.presentation.gallery.GalleryNavRoot
import br.com.mob1st.features.dev.impl.presentation.menu.DevMenuPage
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.navigation.NavRoot

/**
 * Navigation root for the dev settings feature.
 */
object DevSettingsNavRoot : NavRoot {
    context(NavGraphBuilder) override fun graph(navController: NavController) {
        navigation(
            startDestination = DevSettingsNavTarget.DevMenu.screenName,
            route = DevSettingsNavTarget.ROUTE
        ) {
            composable(DevSettingsNavTarget.DevMenu.screenName) {
                DevMenuPage(navController::navigate)
            }
            GalleryNavRoot.graph(navController)
        }
    }
}
