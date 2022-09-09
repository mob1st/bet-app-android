package br.com.mob1st.bet.features.profile

import br.com.mob1st.bet.features.competitions.CompetitionType

data class UserSubscriptionInput(
    val competition: CompetitionForSubscriptionInput,
    val points: Int = 0
)

data class CompetitionForSubscriptionInput(
    val id: String,
    val type: CompetitionType,
    val name: Map<String, String>,
)
