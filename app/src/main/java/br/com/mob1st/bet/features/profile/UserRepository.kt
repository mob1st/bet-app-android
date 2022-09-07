package br.com.mob1st.bet.features.profile

/**
 * Manages the user entity and everything that depends on it to exists
 */
interface UserRepository {

    /**
     * Logs the user anonymously
     *
     * This way the user can start using main features in the app without pass the authentication
     * flow
     */
    suspend fun signInAnonymously(): User

    /**
     * @return AuthStatus in the app
     */
    suspend fun getAuthStatus(): AuthStatus

}