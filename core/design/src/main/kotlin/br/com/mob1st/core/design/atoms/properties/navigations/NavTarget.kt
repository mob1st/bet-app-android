package br.com.mob1st.core.design.atoms.properties.navigations

import androidx.compose.runtime.Immutable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavOptionsBuilder

/**
 * A target to provide the Composable navigation api the information needed to navigate to a specific screen.
 */
@Immutable
abstract class NavTarget {

    /**
     * The screen name used in the navigation to identify this target.
     */
    abstract val screenName: String

    /**
     * The arguments to be passed to the screen.
     */
    val arguments: List<NamedNavArgument> = emptyList()

    /**
     * The deep links to be used to navigate to the screen.
     */
    val deepLinks: List<NavDeepLink> = emptyList()
}

/**
 * Navigates a given [navTarget] with the given [builder] to configure the navigation options.
 */
fun NavController.navigate(navTarget: NavTarget, builder: NavOptionsBuilder.() -> Unit) {
    navigate(navTarget.screenName, builder)
}

/**
 * Navigates a given [navTarget].
 */
fun NavController.navigate(navTarget: NavTarget) {
    navigate(navTarget.screenName)
}
