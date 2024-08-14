package br.com.mob1st.features.finances.publicapi.domain.ui

import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable

/**
 * Builds the navigation graph for the finances feature.
 * It's the entry point for the feature navigation.
 */
interface FinancesNavGraph {
    /**
     * Builds the navigation graph for the finances feature.
     */
    context(NavGraphBuilder)
    fun graph()

    /**
     * The root of the navigation graph.
     */
    @Serializable
    data object Root
}
