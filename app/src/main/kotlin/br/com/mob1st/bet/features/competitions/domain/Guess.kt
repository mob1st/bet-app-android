package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.serialization.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

/**
 * The users will guess the results of confrontations to figure out who is the master of guessing.
 *
 * This is the guess the user can apply for the given [confrontation]. It has the [aggregation] of
 * answers the user are enabled apply.
 */
@Serializable
data class Guess(
    val id: String = "",
    @SerialName("subscriptionRef")
    val subscriptionId: String,
    @Serializable(DateSerializer::class)
    val createdAt: Date = Date(),
    @Serializable(DateSerializer::class)
    val updatedAt: Date = createdAt,
    val confrontation: ConfrontationForGuess,
    val aggregation: AnswerAggregation,
) {
    /**
     * Indicates if the bet
     */
    fun betAllowed() = updatedAt <= confrontation.allowBetsUntil

    /**
     * Update the field [updatedAt] in the case this guess is not new (has id)
     */
    fun update() =
        if (id.isEmpty()) {
            this
        } else {
            copy(updatedAt = Date())
        }
}

/**
 * Nested object for guess
 */
@Serializable
data class ConfrontationForGuess(
    @Serializable
    val competitionId: String,
    @Serializable
    val id: String,
    @Serializable(DateSerializer::class)
    val allowBetsUntil: Date,
) {
    constructor(competitionId: String, confrontation: Confrontation) : this(
        id = confrontation.id,
        allowBetsUntil = confrontation.allowBetsUntil,
        competitionId = competitionId,
    )
}
