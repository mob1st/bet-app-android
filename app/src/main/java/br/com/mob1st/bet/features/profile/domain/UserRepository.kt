package br.com.mob1st.bet.features.profile.domain

import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry

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

}