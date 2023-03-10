package br.com.mob1st.bet.features.profile.data

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.utils.functions.suspendRunCatching
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.profile.domain.AnonymousSignInException
import br.com.mob1st.bet.features.profile.domain.AuthStatus
import br.com.mob1st.bet.features.profile.domain.GetUserException
import br.com.mob1st.bet.features.profile.domain.GetUserFirstAvailableSubscription
import br.com.mob1st.bet.features.profile.domain.User
import br.com.mob1st.bet.features.profile.domain.UserAuth
import br.com.mob1st.bet.features.profile.domain.UserCreationException
import br.com.mob1st.bet.features.profile.domain.UserRepository
import br.com.mob1st.bet.features.profile.domain.UserSubscriptionException
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
internal class UserRepositoryImpl(
    private val userAuth: UserAuth,
    private val userCollection: UserCollection,
    private val dispatcherProvider: DispatcherProvider,
) : UserRepository {

    private val io get() = dispatcherProvider.io

    override suspend fun signInAnonymously(): User = withContext(io) {
        val user = authUser()
        createUser(user)
        return@withContext user
    }

    override suspend fun subscribe(entry: CompetitionEntry): Subscription = withContext(io) {
        suspendRunCatching {
            val userId = checkNotNull(userAuth.get()?.id)
            userCollection.subscribe(
                userId,
                Subscription(competition = entry)
            )
        }.getOrElse {
            throw UserSubscriptionException(entry.id, it)
        }
    }

    override suspend fun get(): User {
        return suspendRunCatching {
            checkNotNull(userAuth.get())
        }.getOrElse { throw GetUserException(it) }
    }

    private suspend fun authUser(): User {
        return suspendRunCatching {
            userAuth.signInAnonymously()
        }.getOrElse {
            throw AnonymousSignInException(it)
        }
    }

    private suspend fun createUser(user: User) = runCatching {
        userCollection.create(user)
    }.getOrElse {
        // sign out the user if the account creation was not possible
        userAuth.signOut()
        throw UserCreationException(user.id, it)
    }

    override suspend fun getFirstAvailableSubscription(): Subscription = withContext(io) {
        suspendRunCatching {
            userCollection.getCompetitionEntry(checkNotNull(userAuth.getId()))
        }.getOrElse {
            throw GetUserFirstAvailableSubscription(it)
        }
    }

    override suspend fun getAuthStatus(): AuthStatus = userAuth.getAuthStatus()
}
