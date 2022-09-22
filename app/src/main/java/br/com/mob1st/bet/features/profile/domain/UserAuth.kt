package br.com.mob1st.bet.features.profile.domain

import arrow.optics.Iso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory

@Factory
class UserAuth(
    private val auth: FirebaseAuth
) {

    private val firebaseIso: Iso<FirebaseUser, User>  = Iso(
        get = { firebaseUser ->
            User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "Anonymous",
                authType = Anonymous,

                // nao sei se isso vai funcionar
                imageUrl = firebaseUser.photoUrl?.toString()
            )
        },
        reverseGet = { _ -> throw Exception() }
    )

    suspend fun signInAnonymously(): User {
        val result = auth.signInAnonymously().await()
        val firebaseUser = checkNotNull(result.user)
        return firebaseIso.get(firebaseUser)
    }

    fun signOut() {
        auth.signOut()
    }

    fun get(): User? {
        return auth.currentUser?.let { firebaseIso.get(it) }
    }

    fun getId(): String? = auth.currentUser?.uid


    fun getAuthStatus(): AuthStatus {
        val currentUser = auth.currentUser
        return when {
            currentUser == null -> LoggedOut
            currentUser.isAnonymous -> Anonymous
            // TODO change this hard coded value further
            else -> LoggedIn(AuthMethod.FACEBOOK)
        }
    }

}