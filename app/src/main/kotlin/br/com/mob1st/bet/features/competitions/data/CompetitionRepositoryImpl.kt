package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.utils.functions.suspendRunCatching
import br.com.mob1st.bet.features.competitions.domain.Competition
import br.com.mob1st.bet.features.competitions.domain.CompetitionRepository
import br.com.mob1st.bet.features.competitions.domain.Confrontation
import br.com.mob1st.bet.features.competitions.domain.GetConfrontationListException
import br.com.mob1st.bet.features.competitions.domain.GetDefaultCompetitionException
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class CompetitionRepositoryImpl(
    private val competitionCollection: CompetitionCollection,
    private val provider: DispatcherProvider,
) : CompetitionRepository {

    private val io get() = provider.io

    override suspend fun getDefaultCompetition(): Competition = withContext(io) {
        suspendRunCatching {
            competitionCollection.getDefault()
        }.getOrElse { throw GetDefaultCompetitionException(it) }
    }

    override suspend fun getConfrontationsBy(
        competitionId: String,
    ): List<Confrontation> = withContext(io) {
        suspendRunCatching {
            competitionCollection.getConfrontationsById(competitionId)
        }.getOrElse { throw GetConfrontationListException(competitionId, it) }
    }
}
