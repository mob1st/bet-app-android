package br.com.mob1st.bet.features.profile.domain

import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.competitions.domain.Guess

/**
 * Manages the user entity and everything that depends on it to exists
 */
interface UserRepository {

    /**
     * Logs the user anonymously
     *
     * This way the user can start using main features in the app without pass the authentication
     * flow
     * @throws AnonymousSignInException
     * @throws UserCreationException
     */
    suspend fun signInAnonymously(): User

    /**
     * Subscribes the user in the given competition
     * @throws UserSubscriptionException
     */
    suspend fun subscribe(entry: CompetitionEntry)

    /**
     * Get the current logged user
     */
    suspend fun get(): User

    /**
     * @return AuthStatus in the app
     */
    suspend fun getAuthStatus(): AuthStatus

    /**
     * Get the first available competition for the User to start the app
     */
    suspend fun getFirstAvailableSubscription(): CompetitionEntry

    /**
     * Place the given guess for logged user.
     * If the id of the guess is empty, then a guess will be created, otherwise the guess will
     * update the fields that can be updated
     */
    suspend fun placeGuess(
        subscriptionId: String,
        guess: Guess,
    )

}