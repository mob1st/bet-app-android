package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.utils.objects.Duo

/**
 * A contest is a predefined structure to allow the user to bet in something
 */
sealed interface Contest

/**
 * When two contenders face each other in a 1x1 confrontation, we have a Duel
 */
interface Duel<T> {
    val contender1: Bet<T>
    val contender2: Bet<T>
    val draw: Bet<Nothing>
}

/**
 * When many possibilities can happen during a confrontation,
 */
interface MultiChoice<T> {
    val contenders: List<Bet<T>>
}

/**
 * A match between two teams
 */
data class FootballMatch(
    override val contender1: Bet<Team>,
    override val contender2: Bet<Team>,
    override val draw: Bet<Nothing>
) : Contest, Duel<Team>

/**
 * The available scores of a Contest
 */
data class IntScores(
    override val contenders: List<Bet<Duo<Int>>>,
) : Contest, MultiChoice<Duo<Int>>