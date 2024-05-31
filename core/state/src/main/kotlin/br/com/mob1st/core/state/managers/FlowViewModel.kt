package br.com.mob1st.core.state.managers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun ViewModel.launchIn(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> Unit,
) {
    viewModelScope.launch(coroutineContext) {
        block()
    }
}
