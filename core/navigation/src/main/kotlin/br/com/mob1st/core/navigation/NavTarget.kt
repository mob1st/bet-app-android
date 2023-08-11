package br.com.mob1st.core.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

/**
 * A target to provide the Composable navigation api the information needed to navigate to a specific screen.
 */
@Immutable
abstract class NavTarget {
    abstract val screenName: String
    open val arguments: List<NamedNavArgument> = emptyList()
    open val deepLinks: List<NavDeepLink> = emptyList()
}
