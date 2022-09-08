package br.com.mob1st.bet.core.timber

import br.com.mob1st.bet.BuildConfig
import br.com.mob1st.bet.core.logs.CrashReportingTool
import timber.log.Timber

class TimberTreeFactory(
    private val crashReportingTool: CrashReportingTool
) {

    operator fun invoke(): Timber.Tree {
        return if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            CrashReportingTree(crashReportingTool)
        }
    }

}