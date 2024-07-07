package br.com.mob1st.tests.featuresutils

import timber.log.Timber

/**
 * A Timber tree that stores logs in memory during tests.
 */
class TestTimberTree : Timber.Tree() {
    private val _logs = mutableListOf<Log>()

    /**
     * The logs that were recorded.
     */
    val logs get() = _logs.toList()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        println("$priority -- $tag: $message")
        _logs.add(Log(priority, tag, message, t))
    }

    /**
     * a log entry from Timber.
     * @property priority the priority of the log.
     * @property tag the tag of the log.
     * @property message the message of the log.
     * @property t the throwable of the log, if any.
     * @see Timber.Tree.log
     */
    data class Log(val priority: Int, val tag: String?, val message: String, val t: Throwable?) {
        /**
         * Checks if the log was sent as a warning, using as reference the value used by Timber when [Timber.w] is
         * called.
         */
        val isWarning get() = priority == WARNING_PRIORITY
    }

    companion object {
        private const val WARNING_PRIORITY = 5
    }
}
