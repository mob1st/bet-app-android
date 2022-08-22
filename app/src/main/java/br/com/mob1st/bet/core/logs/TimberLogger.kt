package br.com.mob1st.bet.core.logs

import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory(binds = [Logger::class])
class TimberLogger(
    private val timberTree: Timber.Tree
) : Logger {

    override fun v(message: String, throwable: Throwable?, vararg params: Any?) {
        timberTree.v(throwable, message, *params)
    }

    override fun d(message: String, throwable: Throwable?, vararg params: Any?) {
        timberTree.d(throwable, message, *params)
    }

    override fun i(message: String, throwable: Throwable?, vararg params: Any?) {
        timberTree.i(throwable, message, *params)
    }

    override fun w(message: String, throwable: Throwable?, vararg params: Any?) {
        timberTree.w(throwable, message, *params)
    }

    override fun e(message: String, throwable: Throwable, vararg params: Any?) {
        timberTree.e(throwable, message, *params)
    }


    override fun child(tag: String): Logger {
        return TimberLogger(Timber.tag(tag))
    }
}