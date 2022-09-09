package br.com.mob1st.bet.features.competitions.data

import br.com.mob1st.bet.core.firebase.getDateNotNull
import br.com.mob1st.bet.core.firebase.getNestedObject
import br.com.mob1st.bet.core.firebase.getStringNotNull
import br.com.mob1st.bet.features.competitions.domain.Competition
import br.com.mob1st.bet.features.competitions.domain.CompetitionType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory

@Factory
class CompetitionCollection(
    private val firestore: FirebaseFirestore
) {

    suspend fun getDefault(): Competition {
        return firestore.competitions
            .whereEqualTo("default", true)
            .whereEqualTo(Competition::type.name, CompetitionType.FOOTBALL.name)
            .get()
            .await()
            .first()
            .let { doc ->
                Competition(
                    id = doc.id,
                    name = doc.getNestedObject(Competition::name.name),
                    code = doc.getStringNotNull(Competition::code.name),
                    startAt = doc.getDateNotNull(Competition::startAt.name),
                    endAt = doc.getDate(Competition::endAt.name),
                    type = CompetitionType.FOOTBALL,
                )
            }
    }

}
val FirebaseFirestore.competitions get() = collection("competitions")