package br.com.mob1st.bet.core.timber

import br.com.mob1st.bet.core.logs.Logger
import timber.log.Timber

/**
 * Timber implementation of [Logger]
 */
class TimberLogger(
    private val forest: Timber.Forest
) : Logger {

    override fun v(message: String, throwable: Throwable?) {
        forest.tag(getCaller()).v(throwable, message)
    }

    override fun d(message: String, throwable: Throwable?) {
        forest.tag(getCaller()).d(throwable, message)
    }

    override fun i(message: String, throwable: Throwable?) {
        forest.tag(getCaller()).i(throwable, message)
    }

    override fun w(message: String, throwable: Throwable?) {
        forest.tag(getCaller()).w(throwable, message)
    }

    override fun e(message: String, throwable: Throwable) {
        forest.tag(getCaller()).e(throwable, message)
    }

    @Suppress("ThrowableNotThrown")
    private fun getCaller(): String {
        val element = Exception().stackTrace[STACK_TRACE_ELEMENT_INDEX]
        val fileNameOnly = element.fileName.split(".")[0]
        return "$fileNameOnly.${element.methodName}"
    }

    companion object {
        // 0 getCaller, 1 is the logger function, 2 is the intercace call
        const val STACK_TRACE_ELEMENT_INDEX = 3
    }

}