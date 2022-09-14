package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.utils.objects.Node
import java.util.Date

/**
 * A confrontation provides bets
 */
data class Confrontation(
    val startAt: Date,
    val expectedDuration: Long,
    val status: CompetitionStatus,
    val contest: Node<Contest>
)

enum class CompetitionStatus {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED,
    CANCELED
}
