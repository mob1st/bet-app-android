package br.com.mob1st.core.kotlinx.collections

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.LinkedList
import java.util.Queue

/**
 * A wrapper for a [MutableStateFlow] that holds a [Queue] of [T].
 * It can be used to queue side effects, such as Snackbars.
 * @param T the type of the [Queue] elements.
 */
class QueueStateFlow<T>(queue: Queue<T> = LinkedList()) : MutableStateFlow<Queue<T>> by MutableStateFlow(queue)

/**
 * Updates the [Queue] of the [QueueStateFlow] by applying the [block] to it.
 * @param block the block to apply to the [Queue].
 * @param T the type of the [Queue] elements.
 */
inline fun <T> QueueStateFlow<T>.updateQueue(block: Queue<T>.() -> Unit) = update {
    it.apply(block)
}
