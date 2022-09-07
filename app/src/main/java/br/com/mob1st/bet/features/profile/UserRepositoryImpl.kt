package br.com.mob1st.bet.features.profile

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.logs.Debuggable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
internal class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dispatcherProvider: DispatcherProvider,
) : UserRepository {

    private val io get() = dispatcherProvider.io
    private val usersCollection get() = firestore.collection("users")

    override suspend fun signInAnonymously(): User = withContext(io) {
        val user = authUser()
        createUser(user.id)
        return@withContext user
    }

    private suspend fun authUser(): User {
        return runCatching {
            val result = auth.signInAnonymously().await()
            val firebaseUser = checkNotNull(result.user)
            User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "Anonymous",
                authType = Anonymous
            )
        }.getOrElse {
            throw AnonymousSignInException(it)
        }
    }

    private suspend fun createUser(id: String) = runCatching {
        usersCollection
            .document(id)
            .set(emptyMap<String, Any>())
            .await()
    }.getOrElse {
        // sign out the user if the account creation was not possible
        auth.signOut()
        throw UserCreationException(id, it)
    }

    override suspend fun getAuthStatus(): AuthStatus {
        val currentUser = auth.currentUser
        return when {
            currentUser == null -> LoggedOut
            currentUser.isAnonymous -> Anonymous
            // TODO change this hard coded value further
            else -> LoggedIn(AuthMethod.FACEBOOK)
        }
    }

}

class AnonymousSignInException(
    cause: Throwable,
) : Exception("Unable to sign in the user anonymously", cause)

class UserCreationException(
    private val authId: String,
    cause: Throwable
) : Exception("unable to create the user on db", cause), Debuggable {
    override fun logProperties(): Map<String, Any> {
        return mapOf("authId" to authId)
    }
}