package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.utils.functions.suspendRunCatching
import br.com.mob1st.bet.features.competitions.domain.Guess
import br.com.mob1st.bet.features.competitions.domain.GuessRepository
import br.com.mob1st.bet.features.competitions.domain.PlaceGuessException
import br.com.mob1st.bet.features.profile.domain.User
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class GuessRepositoryImpl(
    private val guessCollection: GuessCollection,
    private val dispatcherProvider: DispatcherProvider,
) : GuessRepository {

    private val io get() = dispatcherProvider.io

    override suspend fun placeGuess(user: User, guess: Guess) = withContext(io) {
        // uses the app scope to avoid block the user until the guess is placed
        suspendRunCatching {
            if (guess.id.isEmpty()) {
                guessCollection.createGuess(
                    userId = user.id,
                    guess = guess
                )
            } else {
                guessCollection.updateGuess(
                    guess = guess
                )
            }
        }.getOrElse {
            throw PlaceGuessException(guess, it)
        }
    }
}
