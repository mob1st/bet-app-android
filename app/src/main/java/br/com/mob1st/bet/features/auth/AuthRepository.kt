package br.com.mob1st.bet.features.auth

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics,
    private val dispatcherProvider: DispatcherProvider
) {

    private val io get() = dispatcherProvider.io

    suspend fun getAuthType(): AuthType = withContext(io) {
        val currentUser = firebaseAuth.currentUser
        when {
            currentUser == null -> AuthType.SignedOut
            currentUser.isAnonymous -> AuthType.Guest
            else -> AuthType.SignedIn(SignInProvider.GOOGLE)
        }
    }

    suspend fun signInGuestUser(): Unit = withContext(io) {
        val result = firebaseAuth.signInAnonymously().await()
        val user = checkNotNull(result?.user)
        crashlytics.setUserId(user.uid)
        analytics.setUserId(user.uid)
        analytics.setUserProperty("firstName", user.displayName ?: "guest")
    }

}