package br.com.mob1st.bet.features.groups.domain

import br.com.mob1st.bet.core.analytics.AnalyticsEvent
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry

data class CreateGroupEvent(
    val groupEntry: GroupEntry,
    val competitionEntry: CompetitionEntry
) : AnalyticsEvent {
    override val name: String
        get() = "bet_group_creation"

    override fun params(): Map<String, Any> {
        return (groupEntry to competitionEntry).toLogMap()
    }
}
