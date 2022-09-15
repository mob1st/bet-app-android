package br.com.mob1st.bet.features.competitions.domain

import androidx.annotation.Keep
import br.com.mob1st.bet.core.utils.objects.Duo
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
    override val draw: Bet<String>
) : Contest, Duel<Team>

/**
 * The available scores of a Contest
 */
@Serializable
data class IntScores(
    override val contenders: List<Bet<Duo<Int>>>,
) : Contest, MultiChoice<Duo<Int>>