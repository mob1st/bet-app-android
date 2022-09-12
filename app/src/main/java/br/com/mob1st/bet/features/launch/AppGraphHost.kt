package br.com.mob1st.bet.features.launch

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.mob1st.bet.features.home.HomeScreen

/**
 * Destinations for the moment the app is opened
 */
sealed class AppRouteDestination(val route: String) {

    /**
     * The very first page displayed to the user when the app is opened
     */
    object Splash : AppRouteDestination("splash")

    /**
     * The home screen containing the three tabs to allow user navigation
     */
    object Home : AppRouteDestination("home")
}

/**
 * Entry point for the app.
 */
@Composable
fun AppNavGraph() {
    // nav controller to navigate from splash to home
    val launcherNavController = rememberNavController()
    NavHost(
        navController = launcherNavController,
        startDestination = AppRouteDestination.Splash.route
    ) {
        composable(AppRouteDestination.Splash.route) {
            SplashScreen {
                launcherNavController.popBackStack()
                launcherNavController.navigate(AppRouteDestination.Home.route)
            }
        }
        composable(AppRouteDestination.Home.route) {
            HomeScreen()
        }
    }
}