package br.com.mob1st.bet.features.profile.domain

/**
 * The user.
 *
 * Can be you or me
 */
data class User(
    val id: String,
    val name: String,
    val authType: AuthType = Anonymous,
    val activeSubscriptions: Int = 0,
    val membershipCount: Int = 0,
    val imageUrl: String? = null
)

/**
 * Status of authentication. It can be:
 * - logged out
 * - logged in with identifier
 * - anonymously logged in
 */
sealed interface AuthStatus

/**
 * The type of logged authentication the user has. Can be
 * - anonymous
 * - logged in with identifier
 */
sealed interface AuthType {
    val method: AuthMethod
}

/**
 * Indicates the user is not logged yet
 *
 * This is the natural state of a first time user
 */
object LoggedOut : AuthStatus

/**
 * Anonymous auth
 *
 * This is the default the app does after the first launch
 */
object Anonymous : AuthStatus, AuthType {
    override val method: AuthMethod = AuthMethod.ANONYMOUS
}

/**
 * When the user use some intentional authentication action to identify itself
 */
data class LoggedIn(
    override val method: AuthMethod,
) : AuthStatus, AuthType

/**
 * Represents all methods allowed for authentication
 */
enum class AuthMethod {

    ANONYMOUS,
    FACEBOOK,
    PHONE_NUMBER

}



