package br.com.mob1st.bet.core.logs

/**
 * Abstraction of some crash reporter tool
 *
 * Wraps third-party libraries with this interface to be able to log crashes in the app without
 * couple the project with this dependency
 */
interface CrashReportingTool {

    /**
     * Register the logged user in this tool to customize reports
     */
    fun registerUser(userId: String)

    /**
     * Clear the logged user to remove it from crash reports.
     * Call this method when the user logs out
     */
    fun clearUser()

    /**
     * Logs a crash in the tool
     */
    fun log(message: String, throwable: Throwable?)
}
