package br.com.mob1st.features.finances.publicapi.domain.ui

import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable

/**
 * Builds the navigation graph for the category builder flow.
 */
interface BudgetBuilderNavGraph {
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
