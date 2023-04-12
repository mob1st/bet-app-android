package br.com.mob1st.libs.firebase.session

import br.com.mob1st.core.observability.debug.LogToolSession
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseUserSession(
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
) : LogToolSession {

    override fun setUser(userId: String) {
        firebaseCrashlytics.setUserId(userId)
        firebaseAnalytics.setUserId(userId)
    }

    override fun resetUser() {
        firebaseCrashlytics.setUserId("")
        firebaseAnalytics.setUserId(null)
    }
}
