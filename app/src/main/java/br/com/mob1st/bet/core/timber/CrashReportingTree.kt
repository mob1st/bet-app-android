package br.com.mob1st.bet.core.timber

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashReportingTree(
    private val crashlytics: FirebaseCrashlytics,
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }
        crashlytics.setCustomKey(LOG_PRIORITY_KEY, priority)
        crashlytics.log(message)
        if (t != null) {
            crashlytics.recordException(t)
        }
    }

    companion object {
        private const val LOG_PRIORITY_KEY = "LOG_PRIORITY_KEY"
    }
}