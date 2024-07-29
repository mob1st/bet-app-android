package br.com.mob1st.core.state.extensions

import br.com.mob1st.core.state.managers.ErrorHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Creates an [ErrorHandler] that emits a new state with the [updateFailure] function.
 * @param State The type of the state.
 * @param updateFailure Function that receives a [Throwable] and returns a new state.
 * @return The [ErrorHandler] that emits a new state with the [updateFailure] function.
 * @see ErrorHandler
 */
fun <State> MutableStateFlow<State>.errorHandler(
    updateFailure: State.(Throwable) -> State,
): ErrorHandler {
    return ErrorHandler { failure ->
        update { it.updateFailure(failure) }
    }
}
