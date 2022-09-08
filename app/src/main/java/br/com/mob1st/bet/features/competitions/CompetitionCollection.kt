package br.com.mob1st.bet.features.competitions

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromMap
import org.koin.core.annotation.Factory

@OptIn(ExperimentalSerializationApi::class)
@Factory
class CompetitionCollection(
    private val firestore: FirebaseFirestore
) {

    suspend fun getDefault(): Competition {
        return firestore.competitions
            .whereEqualTo("default", true)
            .whereEqualTo("input.${Competition::type.name}", CompetitionType.FOOTBALL.name)
            .get()
            .await()
            .first()
            .let { doc ->
                val map = checkNotNull(doc.data)
                Properties.decodeFromMap(map)
            }
    }

}
val FirebaseFirestore.competitions get() = collection("competitions")