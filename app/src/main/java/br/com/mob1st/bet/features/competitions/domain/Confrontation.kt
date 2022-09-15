package br.com.mob1st.bet.features.competitions.domain

import arrow.optics.optics
import br.com.mob1st.bet.core.utils.objects.Node
import java.util.Date

/**
 * A confrontation provides bets
 */
@optics
data class Confrontation(
    val id: String,
    val startAt: Date,
    val allowBetsUntil: Date,
    val expectedDuration: Long,
    val status: ConfrontationStatus,
    val contest: Node<Contest>
) {
    companion object
}

enum class ConfrontationStatus {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED,
    CANCELED
}
