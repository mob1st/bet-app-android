package br.com.mob1st.bet.features.competitions

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class CompetitionRepositoryImpl(
    private val competitionCollection: CompetitionCollection,
    private val provider: DispatcherProvider,
) : CompetitionRepository {

    private val io get() = provider.io

    override suspend fun getDefaultCompetition(): Competition = withContext(io){
        runCatching {
            competitionCollection.getDefault()
        }.getOrElse { throw GetDefaultCompetitionException(it) }
    }

}