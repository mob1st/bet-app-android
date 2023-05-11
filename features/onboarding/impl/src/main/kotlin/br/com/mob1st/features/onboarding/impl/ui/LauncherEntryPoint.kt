package br.com.mob1st.features.onboarding.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import br.com.mob1st.features.onboarding.impl.domain.SplashDestination

@Composable
fun LauncherEntryPoint() {
    val navigator = rememberNavController()

    NavHost(
        navController = navigator,
        startDestination = LauncherRoute.Splash.screenName
    ) {
        composable(LauncherRoute.Splash.screenName) {
        }
        composable(LauncherRoute.Home.screenName) {
        }
        composable(LauncherRoute.Onboarding.screenName) {
        }
    }
}

fun NavHostController.navigate(route: Route) = navigate(route.screenName)

sealed class LauncherRoute : Route {

    object Splash : LauncherRoute() {
        override val screenName: String = "splash"
    }

    object Home :
        LauncherRoute(),
        DeepLinkable by StandardDeepLinkable("home")

    object Onboarding : LauncherRoute() {
        override val screenName: String = "init-onboarding"
    }

    companion object {
        fun of(splashDestination: SplashDestination) = when (splashDestination) {
            SplashDestination.Home -> Home
            SplashDestination.Onboarding -> Onboarding
        }
    }
}

@Immutable
interface Route {
    val screenName: String
}

interface DeepLinkable : Route {
    val deepLinks: List<NavDeepLink>
}

internal class StandardDeepLinkable(
    override val screenName: String,
) : DeepLinkable {
    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink {
            uriPattern = "https://www.google.com/$screenName"
        }
    )
}
