package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import kotlinx.coroutines.flow.Flow

/**
 * Manage data of inputs for recurrences
 */
internal interface RecurrenceBuilderRepository {
    /**
     * Get the current state of the builder
     */
    fun get(): Flow<RecurrenceBuilder>

    /**
     * Set the current state of the builder
     */
    suspend fun set(builder: RecurrenceBuilder)
}
