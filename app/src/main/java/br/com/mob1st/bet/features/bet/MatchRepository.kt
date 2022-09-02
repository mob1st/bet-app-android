package br.com.mob1st.bet.features.bet

import br.com.mob1st.bet.core.logs.Debuggable
import br.com.mob1st.bet.core.utils.PageFilter
import java.util.Date
import kotlin.jvm.Throws

interface MatchRepository {
    @Throws(GetMatchesException::class)
    suspend fun getFootballMatches(
        date: Date,
        roundId: Round,
        pageFilter: PageFilter,
    ): List<Match<FootballBet>>
}

class GetMatchesException(
    cause: Throwable,
    private val date: Date
) : Exception("unable to fetch the matches", cause), Debuggable {
    override fun logProperties(): Map<String, Any> {
        return mapOf(
            "date" to date
        )
    }
}