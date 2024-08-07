package br.com.mob1st.features.finances.publicapi.domain.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable

/**
 * Builds the navigation graph for the finances feature.
 * It's the entry point for the feature navigation.
 */
interface FinancesNavGraph {
    /**
     * Builds the navigation graph for the finances feature.
     * @param onClickClose Callback to be called when the close button is clicked.
     */
    context(NavGraphBuilder)
    fun graph(
        navController: NavController,
        onClickClose: () -> Unit,
    )

    /**
     * The root of the navigation graph.
     */
    @Serializable
    data object Root
}
