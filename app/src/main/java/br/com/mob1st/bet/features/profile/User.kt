package br.com.mob1st.bet.features.profile

data class User(
    val id: String,
    val name: String,
    val authType: AuthType
)

sealed interface AuthStatus
sealed interface AuthType

object LoggedOut : AuthStatus
object Anonymous : AuthStatus, AuthType
object LoggedIn : AuthStatus, AuthType



