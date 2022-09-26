package br.com.mob1st.bet.features.profile.data

import br.com.mob1st.bet.core.firebase.asJson
import br.com.mob1st.bet.core.firebase.awaitWithTimeout
import br.com.mob1st.bet.features.competitions.data.competitions
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.profile.domain.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.koin.core.annotation.Factory

/**
 * Manages the user and
 */
@Factory
class UserCollection(
    private val firestore: FirebaseFirestore,
    private val json: Json
) {

    /**
     * Subscribe the given [userId] into the competition provided by [input]
     */
    suspend fun subscribe(userId: String, input: Subscription): Subscription {
        val batch = firestore.batch()
        val subscriptionDoc = firestore.subscriptions(userId).document()
        batch.set(
            subscriptionDoc,
            mapOf(
                Subscription::competition.name to mapOf(
                    CompetitionEntry::name.name to input.competition.name,
                    CompetitionEntry::type.name to input.competition.type.name,
                    "ref" to firestore.competitions.document(input.competition.id)
                ),
                Subscription::points.name to input.points,
                Subscription::active.name to input.active,
            ),
        )
        batch.update(
            firestore.users.document(userId),
            mapOf(User::activeSubscriptions.name to FieldValue.increment(1))
        ).commit().awaitWithTimeout()
        return input.copy(id = subscriptionDoc.id)
    }

    suspend fun create(user: User) {
        firestore.users
            .document(user.id)
            .set(mapOf(User::activeSubscriptions.name to 0))
            .awaitWithTimeout()
    }

    suspend fun getCompetitionEntry(userId: String): Subscription {
        val documents = firestore.subscriptions(userId)
            .limit(1)
            .get()
            .awaitWithTimeout()
        return documents.first().let { doc ->
            val jsonObj = doc.asJson()
            json.decodeFromJsonElement(jsonObj)
        }
    }

}

val FirebaseFirestore.users get() =
    collection("users")
fun FirebaseFirestore.subscriptions(userId: String) =
    users.document(userId).collection("subscriptions")

fun FirebaseFirestore.memberships(userId: String) =
    users.document(userId).collection("memberships")

