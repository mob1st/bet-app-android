package br.com.mob1st.features.twocents.builder.impl.domain.infra

import br.com.mob1st.core.database.RecurrenceType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.Builder
import kotlinx.coroutines.flow.Flow

/**
 * Provides data manipulation operations for [Builder] entities.
 */
internal interface BuilderRepository {
    /**
     * Get a builder by its [recurrenceType].
     * Everytime the builder is updated through [set], the [Flow] will emit the new value.
     * @param recurrenceType The recurrence type of the builder.
     * @return A [Flow] of [Builder] with the given [recurrenceType].
     * @see set
     */
    fun getByRecurrenceType(recurrenceType: RecurrenceType): Flow<Builder>

    /**
     * Set a [Builder] entity.
     * After this operation, the places consuming by Flow will be updated.
     * @param builder The [Builder] entity to be set.
     */
    suspend fun set(builder: Builder)
}
