package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.core.utils.objects.Node
import br.com.mob1st.bet.features.competitions.domain.Bet
import br.com.mob1st.bet.features.competitions.domain.CompetitionType
import br.com.mob1st.bet.features.competitions.domain.Contest
import br.com.mob1st.bet.features.competitions.domain.IntScores
import br.com.mob1st.bet.features.competitions.domain.MatchWinner
import br.com.mob1st.bet.features.competitions.domain.Odds
import br.com.mob1st.bet.features.competitions.domain.Team

/**
 * receives a contest in key-value structure and returns the correspoding [Contest] instance based
 * on the [CompetitionType] provided to [ContestMapFactory]
 */
typealias FirebaseContestMapper = (contest: Map<String, Any>) -> Node<Contest>
object ContestMapFactory {

    operator fun get(competitionType: CompetitionType): FirebaseContestMapper {
        return when (competitionType) {
            CompetitionType.FOOTBALL -> { nodeBuilder }
        }
    }
}

private val nodeBuilder: FirebaseContestMapper = { data: Map<String, Any> ->
    Node(
        current = matchWinner(data),
        paths = listOf(
            Node(
                current = matchScore(data),
                paths = emptyList()
            )
        )
    )
}

// TODO figure out how to avoid map property by property in firebase
private val matchWinner = { current: Map<String, Any> ->
     MatchWinner(
         contender1 = Bet(
             odds = Odds.American(0.0),
             subject = Team("", emptyMap(), "")
         ),
         contender2 = Bet(
             odds = Odds.American(0.0),
             subject = Team("", emptyMap(), "")
         ),
         draw = Bet(
             odds = Odds.American(0.0),
             subject = Unit
         )
     )
}

private val bet = { bet: Map<String, Any> ->

}

private val matchScore = { paths: Map<String, Any> ->
    IntScores(
        contenders = emptyList()
    )
}