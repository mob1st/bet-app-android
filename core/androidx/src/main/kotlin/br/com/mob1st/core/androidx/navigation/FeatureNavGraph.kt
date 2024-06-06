package br.com.mob1st.core.androidx.navigation

/**
 * A simple contract for feature Navigation Graph builders.
 */
interface FeatureNavGraph<T> {
    /**
     * The root of the navigation graph.
     * It's used by composable navigation functions to navigate into the feature navigation graph.
     */
    val root: T
}
