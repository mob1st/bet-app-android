package br.com.mob1st.bet.features.profile

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.features.competitions.Competition
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

    override suspend fun subscribe(competition: Competition): Unit = withContext(io){
        runCatching {
            val userId = checkNotNull(userAuth.get()?.id)
            userCollection.subscribe(
                userId,
                UserSubscriptionInput(
                    competition = CompetitionForSubscriptionInput(
                        id = competition.id,
                        name = competition.name,
                        type = competition.type
                    )
                ),
            )
        }.getOrElse {
            throw UserSubscriptionException(competition.id, it)
        }
    }

    override suspend fun get(): User {
        return runCatching {
            checkNotNull(userAuth.get())
        }.getOrElse { throw GetUserException(it) }
    }

    private suspend fun authUser(): User {
        return runCatching {
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

    override suspend fun getAuthStatus(): AuthStatus = userAuth.getAuthStatus()

}