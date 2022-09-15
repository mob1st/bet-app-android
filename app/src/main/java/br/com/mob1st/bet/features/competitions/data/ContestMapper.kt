package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.core.firebase.toJsonObject
import br.com.mob1st.bet.core.utils.objects.Node
import br.com.mob1st.bet.features.competitions.domain.American
import br.com.mob1st.bet.features.competitions.domain.Bet
import br.com.mob1st.bet.features.competitions.domain.CompetitionType
import br.com.mob1st.bet.features.competitions.domain.Contest
import br.com.mob1st.bet.features.competitions.domain.IntScores
import br.com.mob1st.bet.features.competitions.domain.MatchWinner
import br.com.mob1st.bet.features.competitions.domain.Team
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

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

@OptIn(ExperimentalSerializationApi::class)
private val nodeBuilder: FirebaseContestMapper = { data: Map<String, Any> ->
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false

    }
    val nodeJson = data.toJsonObject().jsonObject
    val currentJson = nodeJson["current"]
    val pathsJson = nodeJson["paths"]
    Node<Contest>(
        current = json.decodeFromJsonElement<MatchWinner>(currentJson!!),
        paths = emptyList()
    )
}

private val matchWinner = { current: Map<String, Any> ->
     MatchWinner(
         contender1 = Bet(
             odds = American(0),
             subject = Team("", emptyMap(), "")
         ),
         contender2 = Bet(
             odds = American(0),
             subject = Team("", emptyMap(), "")
         ),
         draw = Bet(
             odds = American(0),
             subject = "DRAW"
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