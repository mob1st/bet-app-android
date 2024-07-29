package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

private const val ANR_TIMEOUT = 5_000L

/**
 * Emits the value at the beginning of the flow.
 * @param T The type of the flow.
 * @param value The value to be emitted.
 * @return The flow with the value emitted at the beginning.
 */
fun <T> Flow<T>.startsWith(value: T) = onStart { emit(value) }

fun <T, R> Flow<List<T>>.mapList(transform: (T) -> R): Flow<List<R>> = map { list ->
    list.map(transform)
}

/**
 * Default sharing strategy for a flow that will be retained.
 * It uses the [SharingStarted.WhileSubscribed] strategy using the ANR timeout to decide if the flow should be
 * restarted when there is no subscribers.
 */
fun <T> Flow<T>.stateInWhileSubscribed(
    scope: CoroutineScope,
    initialValue: T,
): StateFlow<T> = stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(ANR_TIMEOUT),
    initialValue = initialValue,
)
