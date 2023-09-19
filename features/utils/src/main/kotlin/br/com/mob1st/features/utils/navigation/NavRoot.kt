package br.com.mob1st.features.utils.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import br.com.mob1st.core.design.atoms.motion.TransitionPattern

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
    fun graph(
        navController: NavController,
        patterns: List<TransitionPattern> = emptyList(),
    )
}
