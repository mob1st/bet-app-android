package br.com.mob1st.core.state.contracts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the side effect produced by a navigation when it's triggered by a state change.
 */
interface NavigationManager<T> {
    /**
     * The output of the navigation to be consumed by the UI.
     */
    val navigationOutput: StateFlow<T?>

    /**
     * Triggers a navigation.
     */
    context(ViewModel)
    fun goTo(value: T)

    /**
     * Consumes the navigation.
     */
    fun consumeNavigation()
}

class NavigationDelegate<T> : NavigationManager<T> {
    private val _navigationOutput: MutableStateFlow<T?> = MutableStateFlow(null)
    override val navigationOutput: StateFlow<T?> = _navigationOutput.asStateFlow()

    context(ViewModel)
    override fun goTo(value: T) {
        _navigationOutput.value = value
    }

    override fun consumeNavigation() {
        _navigationOutput.value = null
    }
}
