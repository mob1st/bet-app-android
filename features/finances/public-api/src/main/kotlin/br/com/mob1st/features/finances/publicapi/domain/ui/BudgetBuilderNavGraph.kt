package br.com.mob1st.features.finances.publicapi.domain.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable

/**
 * Builds the navigation graph for the category builder flow.
 */
interface BudgetBuilderNavGraph {
    /**
     * Builds the navigation graph for the finances feature.
     * @param navController The navigation controller.
     * @param onComplete Callback to be called the builder flow is completed.
     */
    context(NavGraphBuilder)
    fun graph(
        navController: NavController,
        slideDistance: Int,
        onComplete: () -> Unit,
    )

    /**
     * The root of the navigation graph.
     */
    @Serializable
    data object Root
}
