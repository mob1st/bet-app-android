package br.com.mob1st.bet.features.models.football

data class Score(
    val contender1: ContenderScore,
    val contender2: ContenderScore
)

data class ContenderScore(
    val competitorId: String,
    val value: Int
)