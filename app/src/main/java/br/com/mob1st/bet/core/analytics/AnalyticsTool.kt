package br.com.mob1st.bet.core.analytics

/**
 * Abstraction for third-party analytics tool
 */
interface AnalyticsTool {

    /**
     * Register the logged user in this tool to enrich events
     */
    fun registerUser(userId: String)

    /**
     * Clear the logged user to remove it from crash reports.
     * Call this method when the user logs out
     */
    fun clearUser()

    fun log(event: AnalyticsEvent)
}
