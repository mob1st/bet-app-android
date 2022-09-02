package br.com.mob1st.bet.features.models

import br.com.mob1st.bet.features.models.commons.Bet
import br.com.mob1st.bet.features.models.commons.Score
import br.com.mob1st.bet.features.models.commons.Team
import java.lang.IllegalArgumentException

data class Node<T : Any> (
    val current: T,
    val next: List<Node<T>> = emptyList()
)

sealed interface Contest

interface Duel<T> {

    val contender1: Bet<T>
    val contender2: Bet<T>
    val draw: Bet<Nothing>?

    enum class DuelOption {
        CONTENDER_1,
        CONTENDER_2,
        DRAW
    }

    fun select(option: DuelOption): Bet<*> {
        return when(option) {
            DuelOption.CONTENDER_1 -> contender1
            DuelOption.CONTENDER_2 -> contender2
            DuelOption.DRAW -> draw ?: throw IllegalArgumentException("draw is ")
        }
    }
}

interface MultipleChoice<T> {

    val contenders: List<Bet<T>>

    fun select(option: Int): Bet<*> {
        return contenders[option]
    }

}

data class FootballMatch(
    override val contender1: Bet<Team>,
    override val contender2: Bet<Team>,
    override val draw: Bet<Nothing>?
) : Contest, Duel<Team>

data class MatchScore(
    override val contenders: List<Bet<Score>>
) : Contest, MultipleChoice<Score>

