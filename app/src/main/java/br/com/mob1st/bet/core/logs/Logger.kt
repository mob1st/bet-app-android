package br.com.mob1st.bet.core.logs

/**
 * Logs info in the app
 *
 * It can be used as a replacement of native [android.util.Log], which complicates mocking and can
 * write
 */
interface Logger {

    /**
     * Verbose logs
     *
     * Only when I would be "tracing" the code and trying to find one part of a function
     * specifically.
     * This type of logs should never be sent to production
     */
    fun v(message: String, throwable: Throwable? = null)

    /**
     * Debug logs
     *
     *  Information that is diagnostically helpful to people more than just developers
     *  (IT, sysadmins, etc.).
     * This type of logs should never be sent to production
     */
    fun d(message: String, throwable: Throwable? = null)

    /**
     * Info logs
     *
     * Generally useful information to log (service start/stop, configuration assumptions, etc).
     * Info I want to always have available but usually don't care about under normal circumstances.
     * This is my out-of-the-box config level.
     *
     * These type of logs is sent to production
     */
    fun i(message: String, throwable: Throwable? = null)

    /**
     * Warning logs
     *
     * Anything that can potentially cause application oddities, but for which I am automatically
     * recovering. (Such as switching from a primary to backup server, retrying an operation,
     * missing secondary data, etc.)
     *
     * These type of logs is sent to production
     */
    fun w(message: String, throwable: Throwable? = null)

    /**
     * Error logs
     *
     * Any error which is fatal to the operation, but not the service or application
     * (can't open a required file, missing data, etc.). These errors will force user
     * (administrator, or direct user) intervention. These are usually reserved (in my apps) for
     * incorrect connection strings, missing services, etc
     *
     * These type of logs is sent to production
     */
    fun e(message: String, throwable: Throwable)

}