package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.utils.objects.Duo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Answer<T : Any> {
    val weight: Int
    val odds: Odds
    val selected: T

    fun points() = odds * weight
}

@Serializable
@SerialName("DuelWinner")
data class DuelWinner(
    override val weight: Int,
    override val odds: Odds,
    override val selected: Duel.Selection
) : Answer<Duel.Selection>

@Serializable
@SerialName("FinalScore")
data class FinalScore(
    override val weight: Int,
    override val odds: Odds,
    override val selected: Duo<Int>,
) : Answer<Duo<Int>>