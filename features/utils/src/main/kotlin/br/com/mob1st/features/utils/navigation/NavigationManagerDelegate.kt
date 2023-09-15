package br.com.mob1st.features.utils.navigation

import br.com.mob1st.core.state.contracts.NavigationManager
import br.com.mob1st.core.state.managers.ViewModelDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Delegate that manages the side effect produced by a navigation when it's triggered by a state change.
 */
interface NavigationManagerDelegate<T> : ViewModelDelegate<T?>, NavigationManager {

    /**
     * Triggers a navigation.
     */
    fun trigger(value: T)
}

/**
 * Creates a [NavigationManagerDelegate] instance using the default implementation.
 */
fun <T> NavigationManagerDelegate(): NavigationManagerDelegate<T> = NavigationManagerDelegateImpl()

private class NavigationManagerDelegateImpl<T> : NavigationManagerDelegate<T> {

    private val _output = MutableStateFlow<T?>(null)
    override fun output(scope: CoroutineScope): StateFlow<T?> {
        return _output.asStateFlow()
    }

    override fun trigger(value: T) {
        _output.update { value }
    }

    override fun consumeNavigation() {
        _output.update { null }
    }
}
