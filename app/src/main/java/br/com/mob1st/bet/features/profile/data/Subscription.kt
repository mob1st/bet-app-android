package br.com.mob1st.bet.features.profile.data

import android.os.Parcelable
import androidx.annotation.Keep
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * A user can subscribe some [CompetitionEntry] to receive updated confrontations of it.
 * When it happens, a [Subscription] is created.
 */
@Serializable
@Parcelize
@Keep
data class Subscription(
    val id: String = "",
    val competition: CompetitionEntry,
    val points: Int = 0,
    val active: Boolean = true
) : Parcelable