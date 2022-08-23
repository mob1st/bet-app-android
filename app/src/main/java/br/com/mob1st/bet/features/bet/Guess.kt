package br.com.mob1st.bet.features.bet

import java.util.Date

data class Guess(
    val createdAt: Date,
    val match: Match<*>,
    val value: Int,
)

data class Match<T>(
    val id: String,
    val bet: T,
    val round: Round,
    val date: Date,
    val status: MatchStatus = MatchStatus.NS,
    val allowDraw: Boolean,
)

enum class MatchStatus {
    NS, IN_PROGRESS
}

interface Bet<T> {
    val id: String
    val odds: Double
    val subject: T
}

data class FootballBet(
    val home: TeamBet,
    val away: TeamBet,
    val draw: Bet<Nothing>
)

data class Score(
    val bet: Int,
    val opponent: Int,
)

data class Round(
    val name: String,
    val id: String,
    val allowDraw: Boolean,
)

data class TeamBet(
    override val id: String,
    override val odds: Double,
    override val subject: Team,
    val scores: List<ScoreBet>,
) : Bet<Team>

data class ScoreBet(
    override val id: String,
    override val odds: Double,
    override val subject: Score,
    val score: Score,
) : Bet<Score>

data class Team(
    val id: String,
    val name: String,
    val image: String,
    val code: String,
)

enum class ON_DRAW {
    FINISH,
}