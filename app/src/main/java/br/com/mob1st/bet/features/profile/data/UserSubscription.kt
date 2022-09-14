package br.com.mob1st.bet.features.profile.data

import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry


data class UserSubscriptionInput(
    val competition: CompetitionEntry,
    val points: Int = 0,
    val active: Boolean = true
)