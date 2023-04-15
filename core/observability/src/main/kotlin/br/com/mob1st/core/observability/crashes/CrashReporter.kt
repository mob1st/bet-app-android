package br.com.mob1st.core.observability.crashes

/**
 * A standard interface for crash reporting tools.
 *
 * It can be user as a wrapper for third party crash reporting tools
 */
interface CrashReporter {

    /**
     * Log a crash in the reporting tool.
     * Prefer to use [DebuggableException] in order to provide more context to the logs
     */
    fun crash(throwable: Throwable)
}
