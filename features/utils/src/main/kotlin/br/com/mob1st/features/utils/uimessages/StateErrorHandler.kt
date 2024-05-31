package br.com.mob1st.features.utils.uimessages

import br.com.mob1st.core.state.managers.ErrorHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * An error handler that convert errors into a queue of items.
 * It's useful to display a queue of snackbars or modals.
 * @param T the type of the items in the queue.
 * @param map a function that converts a [Throwable] into a [T].
 */
class StateErrorHandler<T>(
    private val map: (Throwable) -> T,
) : ErrorHandler() {
    private val _state = MutableStateFlow<T?>(null)
    val state = _state.asStateFlow()

    /**
     * Enqueues a new item in the queue.
     */
    override fun display(throwable: Throwable) {
        _state.value = map(throwable)
    }

    fun set(value: T?) {
        _state.value = value
    }
}
