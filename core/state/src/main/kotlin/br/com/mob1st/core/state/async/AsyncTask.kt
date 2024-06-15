package br.com.mob1st.core.state.async

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class AsyncTask(
    private val scope: CoroutineScope,
) {
    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun launchIn(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend () -> Unit,
    ) {
        scope.launch(context) {
            try {
                _loadingState.value = true
                block()
            } finally {
                _loadingState.emit(false)
            }
        }
    }
}
