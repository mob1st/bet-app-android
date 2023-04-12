package br.com.mob1st.core.observability.debug

/**
 * A standard interface for logging.
 */
interface LogToolSession {

    /**
     * Set the user id for crash reporting.
     *
     * Ideally, this should be called when the user logs in.
     */
    fun setUser(userId: String)

    /**
     * Clear the user id for crash reporting.
     *
     * Ideally, this should be called when the user logs out.
     */
    fun resetUser()
}
