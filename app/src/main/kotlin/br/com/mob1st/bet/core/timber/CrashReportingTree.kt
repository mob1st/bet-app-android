package br.com.mob1st.bet.core.timber

import android.util.Log
import br.com.mob1st.bet.core.logs.CrashReportingTool
import timber.log.Timber

class CrashReportingTree(
    private val crashReportingTool: CrashReportingTool,
) : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        crashReportingTool.log(message, t)
    }

    override fun isLoggable(
        tag: String?,
        priority: Int,
    ): Boolean {
        return priority != Log.VERBOSE && priority != Log.DEBUG
    }
}
