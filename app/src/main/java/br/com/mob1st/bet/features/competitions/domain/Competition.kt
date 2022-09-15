package br.com.mob1st.bet.features.competitions.domain

import android.os.Parcelable
import androidx.annotation.Keep
import arrow.optics.optics
import br.com.mob1st.bet.core.localization.LocalizedText
import br.com.mob1st.bet.core.serialization.DateSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date

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
@Keep
@optics
data class CompetitionEntry(
    val id: String,
    val name: LocalizedText,
    val type: CompetitionType,
) : Parcelable {
    companion object
}


@Keep
enum class CompetitionType {
    FOOTBALL
}
