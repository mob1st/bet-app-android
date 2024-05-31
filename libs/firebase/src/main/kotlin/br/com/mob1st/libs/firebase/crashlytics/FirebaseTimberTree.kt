package br.com.mob1st.libs.firebase.crashlytics

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class FirebaseTimberTree : Timber.Tree() {
    private val firebaseCrashReporter = FirebaseCrashReporter(FirebaseCrashlytics.getInstance())

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        t?.let {
            firebaseCrashReporter.crash(it)
        }
    }

    override fun isLoggable(
        tag: String?,
        priority: Int,
    ): Boolean {
        return priority != Log.VERBOSE && priority != Log.DEBUG
    }
}
