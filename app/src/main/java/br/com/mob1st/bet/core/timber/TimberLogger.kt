package br.com.mob1st.bet.core.timber

import br.com.mob1st.bet.core.logs.Logger
import timber.log.Timber

class TimberLogger(
    private val tag: String
) : Logger {

    override fun v(message: String, throwable: Throwable?, vararg params: Any?) {
        Timber.tag(tag).v(throwable, message, *params)
    }

    override fun d(message: String, throwable: Throwable?, vararg params: Any?) {
        Timber.tag(tag).d(throwable, message, *params)
    }

    override fun i(message: String, throwable: Throwable?, vararg params: Any?) {
        Timber.tag(tag).i(throwable, message, *params)
    }

    override fun w(message: String, throwable: Throwable?, vararg params: Any?) {
        Timber.tag(tag).w(throwable, message, *params)
    }

    override fun e(message: String, throwable: Throwable, vararg params: Any?) {
        Timber.tag(tag).e(throwable, message, *params)
    }

}