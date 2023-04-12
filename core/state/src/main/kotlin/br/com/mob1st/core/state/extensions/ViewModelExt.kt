package br.com.mob1st.core.state.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Calls the [MutableSharedFlow.emit] method using the [viewModelScope] to create a new coroutine.
 */
context(ViewModel)
fun <T> MutableSharedFlow<T>.launchEmit(
    value: T,
): Job = viewModelScope.launch {
    emit(value)
}

/**
 * Updates the [MutableStateFlow] with the latest emitted value from the [source] [Flow].
 */
context(ViewModel)
fun <T, U> MutableStateFlow<T>.collectUpdate(
    source: Flow<U>,
    combine: (currentState: T, newData: U) -> T,
): Job = viewModelScope.launch {
    source.onCollect { data ->
        update { currentState ->
            combine(currentState, data)
        }
    }
}

/**
 * Collects the [Flow] and executes the [block] for each emitted value in a separated coroutine.
 */
context(ViewModel)
fun <T> Flow<T>.onCollect(block: suspend (T) -> Unit): Job = onEach {
    block(it)
}.catch {
    Timber.e(it)
}.launchIn(viewModelScope)
