package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.coroutines.AppScopeProvider
import br.com.mob1st.bet.core.logs.Debuggable
import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.features.profile.domain.LoggedOut
import br.com.mob1st.bet.features.profile.domain.User
import br.com.mob1st.bet.features.profile.domain.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

/**
 * Handles the user action of guess placing.
 *
 * It creates or edits a guess, check required the validations, logs the events and transfer the
 * proper data to the datalayer
 */
@Factory
class PlaceGuessUseCase(
    private val userRepository: UserRepository,
    private val guessRepository: GuessRepository,
    private val analyticsTool: AnalyticsTool,
    private val logger: Logger,
    private val appScopeProvider: AppScopeProvider,
) {

    operator fun invoke(
        guess: Guess,
    ) {
        // to don't suspend the UI and allow the user to bet fast, this use case uses the app scope
        appScopeProvider.appScope.launch {
            try {
                val updatedGuess = guess.update()
                val user = requireAuthentication()
                requireAllowedGuess(updatedGuess)
                requireValidAnswers(updatedGuess)
                placeGuess(user, updatedGuess)
            } catch (e: Exception) {
                // once we don't make the user wait the response of this use case, we should catch
                // the error and log it her
                logger.e("error for placing guess", e)
            }

        }
    }

    private suspend fun requireAuthentication(): User {
        if (userRepository.getAuthStatus() == LoggedOut) {
            throw RequireAuthException()
        }
        return userRepository.get()
    }

    private fun requireAllowedGuess(guess: Guess) {
        logger.v("validating guess expiration")
        if (guess.betAllowed()) {
            throw ExpiredBetException(guess)
        }
    }

    private fun requireValidAnswers(guess: Guess) {
        logger.v("validating guess answers")
        if (!guess.aggregation.isValid()) {
            throw InvalidScoreException(guess.aggregation)
        }
    }

    private suspend fun placeGuess(user: User, guess: Guess) {
        logger.i("creating guess")
        guessRepository.placeGuess(
            user = user,
            guess = guess,
        )
        analyticsTool.log(PlaceGuessEvent(guess))
    }

}

/**
 * Thrown when the user tries to create a bet after the expiration limit of the given
 * [guess]
 */
class ExpiredBetException(
    private val guess: Guess
) : IllegalArgumentException("the confrontation ${guess.confrontation.id} does not allows bets anymore"),
    Debuggable {
    override fun logProperties(): Map<String, Any> {
        return mapOf(
            "confrontationId" to guess.confrontation.id,
            "betsAllowedUntil" to guess.confrontation.allowBetsUntil,
            "updatedAt" to guess.updatedAt
        )
    }
}

class InvalidScoreException(
    private val aggregation: AnswerAggregation,
) : IllegalArgumentException("invalid combination of answers"), Debuggable {
    override fun logProperties(): Map<String, Any?> {
        return aggregation.logProperties()
    }
}

class RequireAuthException() : Exception("you need authetication to create a guess")