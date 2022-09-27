package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.core.firebase.awaitWithTimeout
import br.com.mob1st.bet.features.competitions.domain.ConfrontationForGuess
import br.com.mob1st.bet.features.competitions.domain.Guess
import br.com.mob1st.bet.features.profile.data.subscriptions
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.annotation.Factory

@Factory
class GuessCollection(
    private val firestore: FirebaseFirestore
) {

    suspend fun createGuess(
        userId: String,
        guess: Guess,
    ) {
        val confrontation = guess.confrontation

        firestore.guesses
            .document(guess.id)
            .set(mapOf(
                Guess::createdAt::name to guess.createdAt,
                Guess::updatedAt::name to guess.updatedAt,
                Guess::aggregation::name to GuessAnswerFactory.toMap(guess.aggregation),
                Guess::confrontation::name to mapOf(
                    "ref" to firestore
                        .confrontations(confrontation.competitionId)
                        .document(confrontation.id),
                    ConfrontationForGuess::allowBetsUntil to guess.confrontation.allowBetsUntil
                ),
                "subscriptionRef" to mapOf(
                    "ref" to firestore.subscriptions(userId)
                )
            ))
            .awaitWithTimeout()
    }

    suspend fun updateGuess(
        guess: Guess,
    ) {
        firestore.guesses
            .document(guess.id)
            .update(mapOf(
                Guess::updatedAt.name to guess.updatedAt,
                Guess::aggregation.name to GuessAnswerFactory.toMap(guess.aggregation)
            ))
            .awaitWithTimeout()
    }

}

val FirebaseFirestore.guesses get() = collection("guesses")