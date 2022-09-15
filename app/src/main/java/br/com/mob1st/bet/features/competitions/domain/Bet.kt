package br.com.mob1st.bet.features.competitions.domain

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Risk a sum of money or valued item against someone else's on the basis of the outcome of an
 * unpredictable event such as a football match.
 *
 * The [subject] under bet has some probability to happens, typically called as [odds]
 */
@Serializable
@Keep
data class Bet<T>(
    val odds: Odds,
    val subject: T
)

/**
 * There are many possible types of odds.
 * Here you can find some of them
 */
@Serializable
@Keep
sealed class Odds {
    abstract val value: Long

}

@SerialName("EUROPEAN")
@Serializable
@Keep
data class European(override val value: Long) : Odds()

@SerialName("AMERICAN")
@Serializable
@Keep
data class American(override val value: Long) : Odds()