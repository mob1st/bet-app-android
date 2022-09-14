package br.com.mob1st.bet.features.competitions.domain

/**
 * Risk a sum of money or valued item against someone else's on the basis of the outcome of an
 * unpredictable event such as a football match.
 *
 * The [subject] under bet has some probability to happens, typically called as [odds]
 */
data class Bet<T>(
    val odds: Odds,
    val subject: T
)

/**
 * There are many possible types of odds.
 * Here you can find some of them
 */
sealed class Odds {
    abstract val value: Double

    data class European(override val value: Double) : Odds()
    data class American(override val value: Double) : Odds()
}