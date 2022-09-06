package br.com.mob1st.bet.features.profile

interface UserRepository {

    suspend fun signInAnonymously(): User
    suspend fun getAuthStatus(): AuthStatus

}