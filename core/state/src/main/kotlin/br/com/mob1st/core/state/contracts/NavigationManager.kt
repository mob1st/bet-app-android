package br.com.mob1st.core.state.contracts

/**
 * Manages the side effect produced by a navigation when it's triggered by a state change.
 */
fun interface NavigationManager {

    /**
     * Consumes the navigation.
     */
    fun consumeNavigation()
}
