package br.com.mob1st.features.twocents.builder.publicapi

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import br.com.mob1st.core.androidx.navigation.FeatureNavGraph

/**
 * Starts the Builder feature to setup the users' budget.
 */
interface BuilderNavGraph : FeatureNavGraph<BuilderNavGraph.Root> {
    /**
     * The root of the navigation graph.
     */
    context(NavGraphBuilder)
    operator fun invoke(
        navController: NavController,
        onComplete: () -> Unit,
    )

    /**
     * The root of the navigation used to start the navigation from external sources.
     */
    interface Root
}
