package br.com.mob1st.core.state.managers

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A Boolean state flow that indicates if a suspend function is running or not.
 * If it's [value] is true, it means that a suspend function is running, otherwise it's false.
 * It can be useful to indicate loading states in the UI and ensure they are properly handled even if an exception is
 * thrown.
 * @param initialValue The initial value of the state.
 */
class AsyncLoadingState(
    private val initialValue: Boolean = false,
) : MutableStateFlow<Boolean> by MutableStateFlow(initialValue) {
    /**
     * Triggers the given [block] and sets the value to true while the block is running.
     * The [value] is set again to false at the end of the block execution, even if an exception is thrown.
     */
    suspend fun trigger(block: suspend () -> Unit) {
        value = true
        try {
            block()
        } finally {
            value = false
        }
    }
}
