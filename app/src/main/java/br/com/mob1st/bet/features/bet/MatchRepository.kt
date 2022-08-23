package br.com.mob1st.bet.features.bet

interface MatchRepository {
    suspend fun getFootballMatches(): List<Match<FootballBet>>
}