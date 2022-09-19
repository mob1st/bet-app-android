package br.com.mob1st.bet.features.profile.data

import androidx.annotation.Keep
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class UserSubscriptionInput(
    val competition: CompetitionEntry,
    val points: Int = 0,
    val active: Boolean = true
)