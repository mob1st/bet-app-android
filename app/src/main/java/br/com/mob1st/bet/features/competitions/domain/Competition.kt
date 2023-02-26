package br.com.mob1st.bet.features.competitions.domain

import android.os.Parcelable
import androidx.annotation.Keep
import arrow.optics.optics
import br.com.mob1st.bet.core.localization.LocalizedText
import br.com.mob1st.bet.core.serialization.DateSerializer
import java.util.Date
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
@optics
data class Competition(
    val id: String,
    val code: String,
    val name: LocalizedText,
    @Serializable(DateSerializer::class)
    val startAt: Date,
    @Serializable(DateSerializer::class)
    val endAt: Date? = null,
    val type: CompetitionType
) {
    fun toEntry() = CompetitionEntry(id = id, name = name, type = type)
    companion object
}

/**
 * A small piece of the [Competition] entity, typically used for screen navigation or to associate
 * it.
 */
@Parcelize
@optics
@Serializable
@Keep
data class CompetitionEntry(
    @SerialName("ref")
    val id: String,
    val name: LocalizedText,
    val type: CompetitionType
) : Parcelable {
    companion object
}

@Keep
enum class CompetitionType {
    FOOTBALL
}
