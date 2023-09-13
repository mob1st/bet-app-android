package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart

/**
 * Triggers a [Flow] when the [MutableSharedFlow] emits.
 * It will trigger during this [this.onStart] and will cancel the previous [Flow] if it's still running.
 * @param T the type of the [Flow]
 * @param flow the [Flow] to be triggered
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> MutableSharedFlow<Unit>.trigger(flow: () -> Flow<T>): Flow<T> = onStart {
    emit(Unit)
}.flatMapLatest {
    flow()
}
