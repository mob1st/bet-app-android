package br.com.mob1st.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * Root of a navigation graph.
 * It is used to create the navigation graph for a feature.
 */
interface NavRoot {

    /**
     * Create the navigation graph for the feature.
     * @param navController The navigation controller.
     */
    context(NavGraphBuilder)
    fun graph(navController: NavController)
}
