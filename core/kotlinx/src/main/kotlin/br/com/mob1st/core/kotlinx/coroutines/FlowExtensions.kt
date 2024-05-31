package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

/**
 * Triggers a [Flow] when the [MutableSharedFlow] emits.
 * It will trigger during this [this.onStart] and will cancel the previous [Flow] if it's still running.
 * @param T the type of the [Flow]
 * @param flow the [Flow] to be triggered
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> MutableSharedFlow<Unit>.trigger(flow: () -> Flow<T>): Flow<T> =
    onStart {
        emit(Unit)
    }.flatMapLatest {
        flow()
    }

fun <T> MutableStateFlow<PersistentList<T>>.enqueue(item: T) =
    update { list ->
        list + item
    }

fun <T> MutableStateFlow<PersistentList<T>>.dequeue() =
    update {
        it.removeAt(0)
    }

fun <T> MutableSharedFlow<PersistentList<T>>.peek(): Flow<T?> = map { it.firstOrNull() }
