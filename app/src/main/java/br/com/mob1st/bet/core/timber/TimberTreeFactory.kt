package br.com.mob1st.bet.core.timber

import android.util.Log
import br.com.mob1st.bet.BuildConfig
import br.com.mob1st.bet.core.logs.CrashReportingTool
import timber.log.Timber

object TimberTreeFactory {

    fun create(crashReportingTool: CrashReportingTool): Timber.Forest {
        Log.d("ptest", "mey deus vbrioioii")
        return if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            CrashReportingTree(crashReportingTool)
        }.let {
            Timber.plant(it)
            Timber
        }
    }

}