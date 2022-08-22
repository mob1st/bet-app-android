package br.com.mob1st.bet.core.logs

import android.util.Log
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import java.lang.Exception

class CrashReportingTree(
    private val crashlytics: FirebaseCrashlytics
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }
        crashlytics.setCustomKey(LOG_PRIORITY_KEY, priority)
        tag?.let {
            crashlytics.setCustomKey(LOG_TAG_KEY, tag)
        }
        if (t is DebuggableException) {
            crashlytics.setCustomKey("errorCode", t.errorCode)
        }
        CustomKeysAndValues.Builder()
        crashlytics.log(message)
        if (t != null) {
            crashlytics.recordException(t)
        }
    }

    companion object {
        private const val LOG_PRIORITY_KEY = "LOG_PRIORITY_KEY"
        private const val LOG_TAG_KEY = "LOG_TAG_KEY"
    }
}

class DebuggableException(
    val errorCode: Int,
    message: String?,
    cause: Throwable?
) : Exception(message, cause)