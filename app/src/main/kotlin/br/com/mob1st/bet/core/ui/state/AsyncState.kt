package br.com.mob1st.bet.core.ui.state

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
 * @param messages the list of simple messages that should be displayed in the UI. It's typically
 * some generic error message that can be displayed in a text or Snackbar
 */
@Immutable
data class AsyncState<T>(
    val data: T,
    val loading: Boolean = false,
    val messages: List<SimpleMessage> = emptyList(),
) {
    /**
     * removes the given [message] from the state
     * @return a new state without the given [message]
     */
    fun removeMessage(
        message: SimpleMessage,
        loading: Boolean,
    ): AsyncState<T> {
        return copy(messages = messages.filterNot { it.id == message.id }, loading = loading)
    }

    /**
     * Copy this state with a new [data] value.
     *
     * This method is typically called in the success cases of asynchronous operations
     * @param data the new data value
     * @param loading to changes the loading state of this asynchronous operation
     * @return a new state containing the given data value
     */
    fun data(
        data: T,
        loading: Boolean = false,
    ): AsyncState<T> {
        return copy(
            loading = loading,
            data = data,
        )
    }

    /**
     * adds a failure message to be displayed in the ui
     * @param message the message that will be displayed in the UI
     * @param loading by default the loading state is removed. assign true if the default behavior
     * should be changed
     */
    fun failure(
        message: SimpleMessage = SimpleMessage.failure(),
        loading: Boolean = false,
    ): AsyncState<T> {
        return copy(
            messages = messages + message,
            loading = loading,
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
