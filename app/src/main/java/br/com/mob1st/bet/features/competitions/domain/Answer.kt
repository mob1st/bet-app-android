package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.utils.objects.Duo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A confrontation can have many possible answers, each of them have a different [weight] and
 * different [odds].
 *
 * For example, in a football match the user can answer the winner, the score, the top scorer and m
 * more. Each one of them is a different answer, containing different weights.
 */
@Serializable
sealed interface Answer<T : Any> {
    val weight: Int
    val odds: Odds
    val selected: T

    /**
     * multiply the [odds] by the [weight]
     */
    fun points() = odds * weight
}

/**
 * A type of [Answer] that selects the winner of a [Duel]
 */
@Serializable
@SerialName("DuelWinner")
data class DuelWinner(
    override val weight: Int,
    override val odds: Odds,
    override val selected: Duel.Selection,
) : Answer<Duel.Selection>

/**
 * A type of [Answer] that selects the final score of a [Duel]
 */
@Serializable
@SerialName("FinalScore")
data class FinalScore(
    override val weight: Int,
    override val odds: Odds,
    override val selected: Duo<Int>,
) : Answer<Duo<Int>>