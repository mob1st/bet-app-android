package br.com.mob1st.core.kotlinx.structures

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * An entity that can be written and have its changes tracked.
 */
interface Writable {
    /**
     * The registry of the last write operation.
     */
    val writeRegistry: WriteRegistry
}

/**
 * The registry of a write operation.
 * @property createdAt The instant when this registry was created.
 * @property updatedAt The instant when this registry was updated for the last time.
 */
data class WriteRegistry(
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant? = null,
)
