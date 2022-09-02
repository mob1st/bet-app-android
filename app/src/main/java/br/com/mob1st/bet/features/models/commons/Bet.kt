package br.com.mob1st.bet.features.models.commons

data class Bet<T>(
    val odds: Odds,
    val subject: T,
)

sealed class Odds {
    data class American(val value: Double)
    data class Decimal(val value: Double)
}



