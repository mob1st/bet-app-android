package br.com.mob1st.core.design.atoms.properties.navigations

import androidx.compose.runtime.Immutable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

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
    open val arguments: List<NamedNavArgument> = emptyList()

    /**
     * The deep links to be used to navigate to the screen.
     */
    open val deepLinks: List<NavDeepLink> = emptyList()
}
