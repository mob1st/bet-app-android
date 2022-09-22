package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.serialization.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Guess(
    val id: String = "",
    @Serializable(DateSerializer::class)
    val createdAt: Date = Date(),
    @Serializable(DateSerializer::class)
    val updatedAt: Date = createdAt,
    val confrontation: ConfrontationForGuess,
    val subscriptionId: String,
    val aggregation: AnswerAggregation,
) {
    /**
     * Indicates if the bet
     */
    fun betAllowed() = updatedAt <= confrontation.allowBetsUntil

    fun update() = if (id.isEmpty()) {
        this
    } else copy(updatedAt = Date())
}

/**
 * Nested object for guess
 */
@Serializable
data class ConfrontationForGuess(
    @Serializable
    val id: String,
    @Serializable(DateSerializer::class)
    val allowBetsUntil: Date
)