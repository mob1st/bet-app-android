package br.com.mob1st.bet.features.models.commons

import br.com.mob1st.bet.features.models.Contest
import br.com.mob1st.bet.features.models.Node
import java.util.Date
import java.util.LinkedList
import kotlin.time.Duration

/**
 * Rodada 1
 * Rodada 2
 *
 * Paredao 1
 *
 */
data class Round(
    val id: String,
    val name: String,
    val index: Int,
    val startAt: Date,
)
sealed interface Contender
data class Team(
    val id: String,
    val name: String,
    val icon: String,
    val foundation: Date,
    val athletes: List<Athlete>
) : Contender

data class Athlete(
    val id: String,
    val name: String,
    val icon: String,
    val bornDate: Date,
    val country: Country
) : Contender

data class Score(
    val contender1: ContenderScore,
    val contender2: ContenderScore
) : Contender

data class ContenderScore(
    val competitorId: String,
    val value: Number
)

/**
 * World cup
 * BBB
 * UFC
 * Elections
 */
class Competition(
    val start: Date,
    val end: Date?,
    val currentRound: Int,
    val rounds: List<Round>,
    val season: Int
)

data class Confrontation(
    val startAt: Date,
    val allowBetsAt: Date,
    val competition: Competition,
    val round: Round,
    val approxDuration: Duration?,
    val contest: Node<Contest>
)