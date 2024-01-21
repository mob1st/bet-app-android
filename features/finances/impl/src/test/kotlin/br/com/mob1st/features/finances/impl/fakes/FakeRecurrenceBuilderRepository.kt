package br.com.mob1st.features.finances.impl.fakes

import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Fake implementation of [RecurrenceBuilderRepository]
 */
internal class FakeRecurrenceBuilderRepository(
    val setState: MutableStateFlow<RecurrenceBuilder?> = MutableStateFlow(null),
) : RecurrenceBuilderRepository {
    override fun get(): Flow<RecurrenceBuilder> {
        return setState.mapNotNull { it }
    }

    override suspend fun set(builder: RecurrenceBuilder) {
        setState.value = builder
    }
}
