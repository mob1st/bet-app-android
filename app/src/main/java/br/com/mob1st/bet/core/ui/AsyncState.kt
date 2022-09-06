package br.com.mob1st.bet.core.ui

import androidx.compose.runtime.Immutable

/**
 * Asynchronous UI state, used when the UI allow the user to trigger asynchronous operations actions.
 *
 * This is the case of the majority of the UI screens, that typically requires connections to the
 * internet or databases. In these cases, the UI requires some [loading] indication
 * (like a circular progress bar), as well as an indication that operation have
 * failed (using a Snackbar) or succeed.
 *
 * @param T the type of the data this UI will display. Can be any type of data class
 * @param data the resulting data of this asynchronous operation. It's typically assigned in the
 * success cases
 * @param loading a boolean to indicate if this operation is running or not. Typically it is false
 * when the operation have finished
 */
@Immutable
data class AsyncState<T>(
    val data: T,
    val loading: Boolean = false
) {

    /**
     * Copy this state with a new [data] value.
     *
     * This method is typically called in the success cases of asynchronous operations
     * @param data the new data value
     * @param loading to changes the loading state of this asynchronous operation
     * @return a new state containing the given data value
     */
    fun data(data: T, loading: Boolean = false): AsyncState<T>  {
        return copy(
            loading = loading,
            data = data,
        )
    }

    /**
     * Copy this state with a new [loading] value equals true.
     *
     * @param data the value used in the data, if required
     * @return a new state indicating the loading of this UI
     */
    fun loading(data: T = this.data): AsyncState<T> {
        return copy(loading = true, data = data)
    }
}