package br.com.mob1st.bet.core.tooling.flow

import br.com.mob1st.bet.core.tooling.vm.onCollect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

fun <T> MutableSharedFlow<T>.next(
    scope: CoroutineScope,
    value: T,
): Job = scope.launch {
    emit(value)
}

fun <T, U> MutableStateFlow<T>.update(
    scope: CoroutineScope,
    source: Flow<U>,
    combine: (currentState: T, newData: U) -> T,
): Job {
    return source.onCollect(scope) { data ->
        update { currentState ->
            combine(currentState, data)
        }
    }
}

fun <T> Flow<T>.onCollect(scope: CoroutineScope, block: suspend (T) -> Unit): Job {
    return onEach {
        block(it)
    }.catch {
        Timber.e(it)
    }.launchIn(scope)
}

fun <T> Flow<T>.startsWith(value: T) = onStart { emit(value) }
