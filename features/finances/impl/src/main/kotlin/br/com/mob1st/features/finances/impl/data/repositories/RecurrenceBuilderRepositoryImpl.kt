package br.com.mob1st.features.finances.impl.data.repositories

import br.com.mob1st.core.data.UnitaryDataSource
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.data.morphisms.RecurrenceBuilderCache
import br.com.mob1st.features.finances.impl.data.morphisms.toData
import br.com.mob1st.features.finances.impl.data.morphisms.toDomain
import br.com.mob1st.features.finances.impl.data.preferences.RecurrenceBuilderCompletions
import br.com.mob1st.features.finances.impl.data.ram.RecurrenceBuilderLists
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Implementation of [RecurrenceBuilderRepository]
 */
internal class RecurrenceBuilderRepositoryImpl(
    private val listsDataSource: UnitaryDataSource<RecurrenceBuilderLists>,
    private val completionsDataSource: UnitaryDataSource<RecurrenceBuilderCompletions>,
    private val io: IoCoroutineDispatcher,
) : RecurrenceBuilderRepository {
    override fun get(): Flow<RecurrenceBuilder> =
        combine(
            completionsDataSource.data,
            listsDataSource.data,
            ::RecurrenceBuilderCache,
        ).map { it.toDomain() }

    override suspend fun set(builder: RecurrenceBuilder): Unit =
        withContext(io) {
            val (completions, lists) = builder.toData()
            completionsDataSource.set(completions)
            listsDataSource.set(lists)
        }
}
