package br.com.mob1st.core.design.atoms.motion

import androidx.navigation.NavBackStackEntry
import br.com.mob1st.core.design.atoms.properties.navigations.NavTarget

/**
 * Utility to check if a [NavTarget.screenName] matches the given [NavBackStackEntry.destination] route.
 */
internal class NavigationMatches(
    private val initialNavTarget: NavTarget,
    private val destinationNavTarget: NavTarget,
) {
    /**
     * @param initialState The [NavBackStackEntry] to check if it matches the [NavTarget.screenName].
     * @param targetState The [NavBackStackEntry] to check if it matches the [destinationNavTarget] screen name.
     * @return true if the [NavTarget.screenName] matches the [NavBackStackEntry.destination] route, false otherwise.
     */
    operator fun invoke(
        initialState: NavBackStackEntry,
        targetState: NavBackStackEntry,
    ): Boolean {
        return initialNavTarget invoke initialState && destinationNavTarget invoke targetState
    }

    private infix fun NavTarget.invoke(entry: NavBackStackEntry): Boolean {
        return screenName == entry.destination.route
    }
}
