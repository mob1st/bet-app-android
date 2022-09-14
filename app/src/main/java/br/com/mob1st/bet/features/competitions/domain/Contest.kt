package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.utils.objects.Duo

/**
 * A contest is a predefined structure to allow the user to bet in something
 */
sealed interface Contest

/**
 * A match between two teams
 */
data class MatchWinner(
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