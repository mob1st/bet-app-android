package br.com.mob1st.bet.features.auth

sealed class AuthType(val signed: Boolean) {

    data class SignedIn(val provider: SignInProvider) : AuthType(true)
    object Guest : AuthType(true)
    object SignedOut : AuthType(false)

}

enum class SignInProvider {
    GOOGLE, FACEBOOK, EMAIL
}