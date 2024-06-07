package br.com.mob1st.features.twocents.builder.impl.infra

import br.com.mob1st.core.database.RecurrenceType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.Builder
import br.com.mob1st.features.twocents.builder.impl.domain.infra.BuilderRepository
import kotlinx.coroutines.flow.Flow

internal class BuilderRepositoryImpl : BuilderRepository {
    override fun getByRecurrenceType(recurrenceType: RecurrenceType): Flow<Builder> {
        TODO("Not yet implemented")
    }

    override suspend fun set(builder: Builder) {
        TODO("Not yet implemented")
    }
}
