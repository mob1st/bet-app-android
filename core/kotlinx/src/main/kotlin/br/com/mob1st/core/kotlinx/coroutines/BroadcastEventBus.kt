package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * An event bus that broadcasts events to all subscribers.
 */
abstract class BroadcastEventBus<T> {

    private val _events = MutableSharedFlow<T>()

    /**
     * A read-only public view of the event bus.
     */
    val events = _events.asSharedFlow()

    /**
     * Posts an event to all subscribers.
     */
    suspend fun postEvent(event: T) {
        _events.emit(event)
    }
}
