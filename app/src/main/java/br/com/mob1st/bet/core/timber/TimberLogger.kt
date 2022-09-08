package br.com.mob1st.bet.core.timber

import br.com.mob1st.bet.core.logs.Logger
import timber.log.Timber

/**
 * Timber implementation of [Logger]
 */
class TimberLogger(
    private val tag: String
) : Logger {

    override fun v(message: String, throwable: Throwable?) {
        Timber.tag(tag).v(throwable, message)
    }

    override fun d(message: String, throwable: Throwable?) {
        Timber.tag(tag).d(throwable, message)
    }

    override fun i(message: String, throwable: Throwable?) {
        Timber.tag(tag).i(throwable, message)
    }

    override fun w(message: String, throwable: Throwable?) {
        Timber.tag(tag).w(throwable, message)
    }

    override fun e(message: String, throwable: Throwable) {
        Timber.tag(tag).e(throwable, message)
    }

}