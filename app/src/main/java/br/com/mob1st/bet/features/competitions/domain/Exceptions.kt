package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.logs.Debuggable

class GetDefaultCompetitionException(
    cause: Throwable
) : Exception("unable to get the default competition", cause)

class GetConfrontationListException(
    private val id: String,
    cause: Throwable
) : Exception("unable to get the competition id $id", cause), Debuggable {
    override fun logProperties(): Map<String, Any> {
        return mapOf("competitionId" to id)
    }

}