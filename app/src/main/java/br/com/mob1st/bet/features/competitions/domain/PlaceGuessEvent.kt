package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.analytics.AnalyticsEvent
import br.com.mob1st.bet.core.arrow.dateTimeIso

/**
 * Triggered when the user places a guess
 */
data class PlaceGuessEvent(
    private val guess: Guess
) : AnalyticsEvent {
    override val name: String
        get() = "bet_place_guess"

    override fun params(): Map<String, Any?> {
        return mapOf(
            "moment" to dateTimeIso.get(guess.updatedAt),
            "betAllowedUntil" to dateTimeIso.get(guess.confrontation.allowBetsUntil),
            "confrontationId" to guess.confrontation.id,
            "subscriptionId" to guess.subscriptionId
        ) + guess.aggregation.logProperties()
    }

}
