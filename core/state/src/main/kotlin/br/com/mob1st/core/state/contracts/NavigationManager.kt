package br.com.mob1st.core.state.contracts

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the side effect produced by a navigation when it's triggered by a state change.
 */
interface NavigationManager<T> {
    val navigationOutput: StateFlow<T?>

    /**
     * Consumes the navigation.
     */
    fun consumeNavigation()
}

/**
 * Delegate that manages the side effect produced by a navigation when it's triggered by a state change.
 */
interface NavigationDelegate<T> : NavigationManager<T> {
    /**
     * Triggers a navigation.
     */
    fun goTo(value: T)
}

/**
 * Creates a [NavigationDelegate] instance using the default implementation.
 */
fun <T> NavigationDelegate(navigationOutput: MutableStateFlow<T?> = MutableStateFlow(null)): NavigationDelegate<T> =
    NavigationDelegateImpl(
        _navigationOutput = navigationOutput,
    )

private class NavigationDelegateImpl<T>(
    private val _navigationOutput: MutableStateFlow<T?>,
) : NavigationDelegate<T> {
    override val navigationOutput: StateFlow<T?> = _navigationOutput.asStateFlow()

    override fun goTo(value: T) {
        _navigationOutput.value = value
    }

    override fun consumeNavigation() {
        _navigationOutput.value = null
    }
}
