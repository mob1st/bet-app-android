package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.features.competitions.domain.AnswerAggregation
import br.com.mob1st.bet.features.competitions.domain.DuelWinner
import br.com.mob1st.bet.features.competitions.domain.FinalScore
import br.com.mob1st.bet.features.competitions.domain.Odds
import br.com.mob1st.bet.features.competitions.domain.WinnerAnswers

/**
 * Creates a strucure of answer in firestore depending on the given type
 */
object GuessAnswerFactory {
    fun toMap(answerAggregation: AnswerAggregation): Map<String, Any> {
        return when (answerAggregation) {
            is WinnerAnswers -> answerAggregation.toMap()
        }
    }
}

private fun WinnerAnswers.toMap(): Map<String, Any> {
    return mapOf(
        WinnerAnswers::winner.name to
            mapOf(
                DuelWinner::selected.name to winner.selected.name,
                DuelWinner::weight.name to winner.weight,
                winner.odds.toField(DuelWinner::odds.name),
            ),
    ).plus(
        score?.let {
            mapOf(
                WinnerAnswers::score.name to
                    mapOf(
                        FinalScore::selected.name to
                            mapOf(
                                "first" to it.selected.first,
                                "second" to it.selected.second,
                            ),
                        FinalScore::weight.name to it.weight,
                        it.odds.toField(FinalScore::odds.name),
                    ),
            )
        } ?: emptyMap(),
    )
}

private fun Odds.toField(fieldName: String): Pair<String, Map<String, Any>> {
    return fieldName to
        mapOf(
            Odds::value.name to value,
        )
}
