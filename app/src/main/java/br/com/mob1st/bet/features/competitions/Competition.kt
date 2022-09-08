package br.com.mob1st.bet.features.competitions

import br.com.mob1st.bet.core.serialization.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Competition(
    val id: String,
    val code: String,
    val name: Map<String, String>,
    val season: Int,
    @Serializable(DateSerializer::class)
    val startAt: Date,
    @Serializable(DateSerializer::class)
    val endAt: Date? = null,
    val type: CompetitionType
)

enum class CompetitionType {
    FOOTBALL
}
