package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.tooling.ktx.Node
import java.util.Date

/**
 * A confrontation provides bets
 */
data class Confrontation(
    val id: String,
    val startAt: Date,
    val allowBetsUntil: Date,
    val expectedDuration: Long,
    val status: ConfrontationStatus,
    val contest: Node<Contest>
)

enum class ConfrontationStatus {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED,
    CANCELED
}
