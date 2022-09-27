package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.logs.Debuggable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Aggregates all answers for a specific contest, making possible to sum all of them in order to
 * provide the possible total points the user can achieve in a guess
 */
@Serializable
sealed interface AnswerAggregation : Debuggable {

    /**
     * all selected answers of the user
     */
    val answers: Set<Answer<*>>

    /**
     * the sum of all points of each answer in [answers]
     */
    fun totalPoints(): Long= answers.sumOf { it.points() }

    /**
     * Indicates if the selected answers available in [answers] are valid
     */
    fun isValid(): Boolean
}

/**
 * It happens when a user tries to predict the result of some bet
 */
@Serializable
@SerialName("WinnerAnswers")
data class WinnerAnswers(
    val winner: DuelWinner?,
    val score: FinalScore? = null,
) : AnswerAggregation {
    override val answers: Set<Answer<*>>
        get() = setOfNotNull(winner, score)

    override fun isValid(): Boolean {
        return when {
            winner == null -> false
            score == null -> true
            winner.selected == Duel.Selection.DRAW -> score.selected.first == score.selected.second
            else -> score.selected.first != score.selected.second
        }
    }

    override fun logProperties(): Map<String, Any?> {
        return mapOf(
            "winner" to winner?.selected,
            "score1" to score?.selected?.first,
            "score2" to score?.selected?.second
        )
    }
}