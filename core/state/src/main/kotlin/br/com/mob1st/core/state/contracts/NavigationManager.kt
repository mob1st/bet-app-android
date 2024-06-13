package br.com.mob1st.core.state.contracts

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the side effect produced by a navigation when it's triggered by a state change.
 */
interface NavigationManager<T> {
    /**
     * Handy method for ViewModels to directly show the navigation.
     */
    fun goTo(navigation: T)

    /**
     * The output of the navigation to be consumed by the UI.
     */
    val navigationOutput: StateFlow<T?>

    /**
     * Consumes the navigation.
     */
    fun consumeNavigation()
}

class NavigationDelegate<T> : NavigationManager<T>, MutableStateFlow<T?> by MutableStateFlow(null) {
    override val navigationOutput: StateFlow<T?> = asStateFlow()

    override fun goTo(navigation: T) {
        value = navigation
    }

    override fun consumeNavigation() {
        value = null
    }
}
