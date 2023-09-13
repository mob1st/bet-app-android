package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * An event bus that broadcasts events to all subscribers.
 */
interface BroadcastEventBus<T> {

    /**
     * A read-only public view of the event bus.
     */
    val events: Flow<T>

    /**
     * Posts an event to all subscribers.
     */
    suspend fun postEvent(event: T)
}

/**
 * Creates a new [BroadcastEventBus] using the default implementation.
 */
fun <T> BroadcastEventBus(): BroadcastEventBus<T> = BroadcastEventBusImpl()

private class BroadcastEventBusImpl<T> : BroadcastEventBus<T> {

    private val _events = MutableSharedFlow<T>()
    override val events: Flow<T> = _events.asSharedFlow()

    override suspend fun postEvent(event: T) {
        _events.emit(event)
    }
}
