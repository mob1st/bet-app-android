package br.com.mob1st.bet.features.competitions.domain

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.logs.Debuggable
import br.com.mob1st.bet.core.logs.Logger
import org.koin.core.annotation.Factory

@Factory
class PlaceGuessUseCase(
    private val guessRepository: GuessRepository,
    private val analyticsTool: AnalyticsTool,
    private val logger: Logger,
) {

    suspend operator fun invoke(
        guess: Guess,
    ): Guess {
        val updatedGuess = guess.update()
        requireAllowedGuess(updatedGuess)
        requireValidAnswers(updatedGuess)
        placeGuess(updatedGuess)
        return updatedGuess
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

    private suspend fun placeGuess(guess: Guess) {
        logger.i("creating guess")
        guessRepository.set(guess = guess)
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