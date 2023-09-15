package br.com.mob1st.tests.featuresutils

import br.com.mob1st.features.utils.navigation.NavigationManagerDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeNavigationManagerDelegate<T>(
    private val output: MutableStateFlow<T?> = MutableStateFlow(null),
) : NavigationManagerDelegate<T> {

    /**
     * The list of values that were passed to [trigger].
     */
    val triggers: List<T> get() = _triggers.toList()
    private val _triggers = mutableListOf<T>()

    /**
     * The number of times [consumeNavigation] was called.
     */
    val consumesCount: Int get() = _consumes.size
    private val _consumes = mutableListOf<Unit>()

    override fun output(scope: CoroutineScope): StateFlow<T?> {
        return output.asStateFlow()
    }

    override fun trigger(value: T) {
        _triggers += value
        output.update { value }
    }

    override fun consumeNavigation() {
        _consumes += Unit
        output.update { null }
    }
}

/**
 * @return true if [FakeNavigationManagerDelegate.trigger] was called at least once, false otherwise.
 */
fun <T> FakeNavigationManagerDelegate<T>.isTriggered(): Boolean = triggers.isNotEmpty()

/**
 * @return true if [FakeNavigationManagerDelegate.consumeNavigation] was called at least once, false otherwise.
 */
fun <T> FakeNavigationManagerDelegate<T>.isConsumed(): Boolean = consumesCount > 0
