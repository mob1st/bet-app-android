package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.logs.Debuggable
import br.com.mob1st.bet.features.profile.domain.User

/**
 * Manages CRUD operations on Guess entity and everything that depends on it to exists
 */
interface GuessRepository {
    /**
     * Place the given guess for logged user.
     * If the id of the guess is empty, then a guess will be created, otherwise the guess will
     * update the fields that can be updated
     */
    suspend fun placeGuess(
        user: User,
        guess: Guess,
    )
}

class PlaceGuessException(
    private val guess: Guess,
    cause: Throwable,
) : Exception("unable to create a guess", cause), Debuggable {
    override fun logProperties(): Map<String, Any?> {
        return mapOf(
            "subscription" to guess.subscriptionId,
            "confrontation" to guess.confrontation.id,
            "confrontationAllowBetsUntil" to guess.confrontation.allowBetsUntil,
            "createdAt" to guess.createdAt,
            "updatedAt" to guess.updatedAt,
        ) + guess.aggregation.logProperties()
    }
}
