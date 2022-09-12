package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.localization.LocalizedText
import java.util.Date

data class Competition(
    val id: String,
    val code: String,
    val name: LocalizedText,
    val startAt: Date,
    val endAt: Date? = null,
    val type: CompetitionType
)

enum class CompetitionType {
    FOOTBALL
}
