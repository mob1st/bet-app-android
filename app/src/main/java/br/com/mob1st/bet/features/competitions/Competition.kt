package br.com.mob1st.bet.features.competitions

import java.util.Date

data class Competition(
    val id: String,
    val code: String,
    val name: Map<String, String>,
    val startAt: Date,
    val endAt: Date? = null,
    val type: CompetitionType
)

enum class CompetitionType {
    FOOTBALL
}
