package br.com.mob1st.bet.features.competitions.domain

import androidx.annotation.Keep
import br.com.mob1st.bet.core.tooling.ktx.Duo
import br.com.mob1st.bet.core.tooling.ktx.Selector
import kotlinx.serialization.Serializable

/**
 * A contest is a predefined structure to allow the user to bet in something
 */
@Serializable
sealed interface Contest

/**
 * A match between two teams
 */
@Serializable
@Keep
data class MatchWinner(
    override val contender1: Bet<Team>,
    override val contender2: Bet<Team>,
    override val draw: Bet<String>,
    val scores: List<IntScores> = emptyList()
) : Contest, Duel<Team>, Selector<Duel.Selection, DuelWinner> {

    override fun select(selection: Duel.Selection): DuelWinner {
        val bet = when(selection) {
            Duel.Selection.CONTENDER_1 -> contender1
            Duel.Selection.CONTENDER_2 -> contender2
            Duel.Selection.DRAW -> draw
        }
        return DuelWinner(
            odds = bet.odds,
            // todo find a way to customize it
            weight = 1,
            selected = selection,
        )
    }
}

/**
 * The available scores of a Contest
 */
@Serializable
@Keep
data class IntScores(
    override val contenders: List<Bet<Duo<Int>>>,
) : Contest, MultiChoice<Duo<Int>>, Selector<Duo<Int>, FinalScore> {
    override fun select(selection: Duo<Int>): FinalScore {
        val bet = contenders.firstOrNull { it.subject == selection } ?: contenders.maxBy { it.odds }
        return FinalScore(
            odds = bet.odds,
            // todo find a way to customize it
            weight = 1,
            selected = selection
        )
    }


}