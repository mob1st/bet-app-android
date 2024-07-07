package br.com.mob1st.tests.featuresutils

import timber.log.Timber

class TestTimberTree : Timber.Tree() {
    private val _logs = mutableListOf<Log>()
    val logs get() = _logs.toList()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        println("$priority -- $tag: $message")
        _logs.add(Log(priority, tag, message, t))
    }

    data class Log(val priority: Int, val tag: String?, val message: String, val t: Throwable?) {
        val isWarning get() = priority == WARNING_PRIORITY
    }

    inline fun <reified T> hasError() = logs.any { it.t is T }

    companion object {
        private const val WARNING_PRIORITY = 5
    }
}
