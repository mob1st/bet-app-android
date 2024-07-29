package br.com.mob1st.core.androidx.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Launches a coroutine in the [viewModelScope].
 * @param coroutineContext The context of the coroutine.
 * @param block The block of code to be executed.
 */
fun ViewModel.launchIn(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> Unit,
) {
    viewModelScope.launch(coroutineContext) {
        block()
    }
}
