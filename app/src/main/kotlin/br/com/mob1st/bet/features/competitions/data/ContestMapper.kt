package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.core.firebase.toJsonObject
import br.com.mob1st.bet.core.tooling.ktx.Node
import br.com.mob1st.bet.features.competitions.domain.CompetitionType
import br.com.mob1st.bet.features.competitions.domain.Contest
import br.com.mob1st.bet.features.competitions.domain.IntScores
import br.com.mob1st.bet.features.competitions.domain.MatchWinner
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * receives a contest in key-value structure and returns the correspoding [Contest] instance based
 * on the [CompetitionType] provided to [ContestMapFactory]
 */
typealias FirebaseContestMapper = (contest: Map<String, Any>) -> Node<Contest>

object ContestMapFactory {
    operator fun get(competitionType: CompetitionType): FirebaseContestMapper {
        return when (competitionType) {
            CompetitionType.FOOTBALL -> {
                nodeBuilder
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
private val nodeBuilder: FirebaseContestMapper = { data: Map<String, Any> ->
    val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    val nodeJson = data.toJsonObject()
    val currentJson = checkNotNull(nodeJson["current"])
    val pathsJson = checkNotNull(nodeJson["paths"])
    Node(
        current = json.decodeFromJsonElement<MatchWinner>(currentJson),
        paths = json.decodeFromJsonElement<List<Node<IntScores>>>(pathsJson),
    )
}
