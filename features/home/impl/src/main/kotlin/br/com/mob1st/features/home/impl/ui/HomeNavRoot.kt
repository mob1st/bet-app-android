package br.com.mob1st.features.home.impl.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.mob1st.features.home.publicapi.ui.HomeNavTarget
import br.com.mob1st.features.utils.navigation.NavRoot

/**
 * Home navigation root.
 */
object HomeNavRoot : NavRoot {

    context(NavGraphBuilder) override fun graph(navController: NavController) {
        navigation(
            startDestination = HomeNavTarget.Home.screenName,
            route = HomeNavTarget.ROUTE
        ) {
            composable(HomeNavTarget.Home.screenName) {
                HomePage()
            }
        }
    }
}
