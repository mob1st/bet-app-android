package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.core.firebase.asJson
import br.com.mob1st.bet.core.firebase.awaitWithTimeout
import br.com.mob1st.bet.core.firebase.getDateNotNull
import br.com.mob1st.bet.core.firebase.getLongNotNull
import br.com.mob1st.bet.core.firebase.getNestedObject
import br.com.mob1st.bet.core.firebase.getStringNotNull
import br.com.mob1st.bet.features.competitions.domain.Competition
import br.com.mob1st.bet.features.competitions.domain.CompetitionType
import br.com.mob1st.bet.features.competitions.domain.Confrontation
import br.com.mob1st.bet.features.competitions.domain.ConfrontationStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.koin.core.annotation.Factory

@Factory
class CompetitionCollection(
    private val firestore: FirebaseFirestore,
    private val json: Json,
) {

    suspend fun getDefault(): Competition {
        val documents = firestore.competitions
            .whereEqualTo("default", true)
            .whereEqualTo(Competition::type.name, CompetitionType.FOOTBALL.name)
            .limit(1)
            .get()
            .awaitWithTimeout()
        return documents.first().let { doc ->
            val obj = doc.asJson()
            json.decodeFromJsonElement(obj)
        }
    }

    suspend fun getConfrontationsById(competitionId: String): List<Confrontation> {
        val confrontations = firestore.confrontations(competitionId)
            .get()
            .awaitWithTimeout()
        return confrontations.map { doc ->
            //TODO: Fix the database to allow sealed class mapping of Confrontation
            val contest = doc.getNestedObject<Any>(Confrontation::contest.name)
            Confrontation(
                id = doc.id,
                expectedDuration = doc.getLongNotNull(Confrontation::expectedDuration.name),
                allowBetsUntil = doc.getDateNotNull(Confrontation::allowBetsUntil.name),
                startAt = doc.getDateNotNull(Confrontation::startAt.name),
                status = ConfrontationStatus.valueOf(doc.getStringNotNull(Confrontation::status.name)),
                contest = ContestMapFactory[CompetitionType.FOOTBALL](contest)
            )
        }
    }

}

val FirebaseFirestore.competitions get() = collection("competitions")
fun FirebaseFirestore.confrontations(competitionId: String) =
    collection("competitions/${competitionId}/confrontations")
