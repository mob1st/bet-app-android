package br.com.mob1st.features.auth.publicapi.domain

/**
 * Indicates the current status of the user's authentication.
 * @param hasAccess indicates whether the user has access to the main features.
 */
enum class AuthStatus(val hasAccess: Boolean) {
    /**
     * The user is logged in and has access to the main features.
     */
    LOGGED_IN(true),

    /**
     * The user is logged as anonymous and has limited access to the main features.
     */
    ANONYMOUS(true),

    /**
     * The user is logged out and has no access to the main features.
     */
    LOGGED_OUT(false),
}
