package br.com.mob1st.bet.core.ui

import androidx.compose.runtime.Immutable
import java.util.UUID

/**
 * Asynchronous UI state, used when the UI allow the user to trigger asynchronous operations actions.
 *
 * This is the case of the majority of the UI screens, that typically requires connections to the
 * internet or databases. In these cases, the UI requires some [loading] indication
 * (like a circular progress bar), as well as an indication that operation have
 * failed (using a Snackbar) or succeed.
 *
 * @param T the type of the data this UI will display. Can be any type of data class
 * @param data The data itself. Typically it represents the success state of this asynchronous
 * operation
 * @param loading a boolean to indicate if this operation is running or not. Typically it is false
 * when the operation have finished
 * @param singleShots the list of pieces of state that should be consumed just once. This class
 * by default will execute them in a stack order.
 * @see SingleShot
 */
@Immutable
data class AsyncState<T>(
    val data: T,
    val loading: Boolean = false,
    val singleShots: List<SingleShot<*>> = emptyList(),
) {

    /**
     * removes the given [event] from the state
     * @return a new state without the given [event]
     */
    fun removeSingleShot(event: SingleShot<*>): AsyncState<T> {
        return copy(singleShots = removeEvent(event))
    }

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
     * Copy this state with a new [SingleShot] instance added to its [singleShots] list.
     *
     * This method is typically called when an asynchronous operation have failed and a simple error
     * message should be displayed in the UI.
     * @param loading to changes the loading state of this asynchronous operation
     * @return a new state containing the error message
     */
    fun error(loading: Boolean = false): AsyncState<T> {
        return copy(
            singleShots = singleShots + errorMessage(),
            loading = loading
        )
    }

    /**
     * Copy this state with a new [loading] value equals true.
     *
     * @param data the value used in the data, if required
     * @return a new state indicating the loading of this UI
     */
    fun star(data: T = this.data): AsyncState<T> {
        return copy(loading = true, data = data)
    }

    private fun removeEvent(event: SingleShot<*>): List<SingleShot<*>> {
        return singleShots.filterNot { it.id == event.id }
    }
}

/**
 * Represents a piece of the state of the UI that should be consumed just once.
 *
 * In some situations, pieces of the state of the UI should be consumed only one time, event if the
 * UI lifecycle is restarted. Examples can be an SnackBar or Dialog that should be displayed to the
 * user to show an general error message, or changes in the state of the UI that requires
 * navigation to another screen.
 *
 * For these cases, wrap this piece of the state with this interface, so it will be automatically
 * handled by the [StateViewModel] and the default UI functionalities of this project.
 *
 * @param T the data of the data that will be consumed
 * @see StateViewModel
 */
@Immutable
interface SingleShot<T> {

    /**
     * The identifier of the state
     *
     * It can be useful for cases where
     */
    val id: UUID

    /**
     * The data that should be consumed by UI
     */
    val data: T
}