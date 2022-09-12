package br.com.mob1st.bet.features.profile.data

import br.com.mob1st.bet.features.competitions.domain.Competition
import br.com.mob1st.bet.features.competitions.data.competitions
import br.com.mob1st.bet.features.profile.domain.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory

/**
 * Manages the user and
 */
@Factory
class UserCollection(
    private val firestore: FirebaseFirestore
) {

    /**
     * Subscribe the given [userId] into the competition provided by [input]
     */
    suspend fun subscribe(userId: String, input: UserSubscriptionInput) {
        val batch = firestore.batch()
        batch.set(
            firestore.subscriptions(userId).document(),
            mapOf<String, Any>(
                UserSubscriptionInput::competition.name to mapOf(
                    Competition::name.name to input.competition.name,
                    Competition::type.name to input.competition.type.name,
                    "ref" to firestore.competitions.document(input.competition.id)
                ),
                UserSubscriptionInput::points.name to input.points
            ),
        )
        batch.update(
            firestore.users.document(userId),
            mapOf(User::activeSubscriptions.name to FieldValue.increment(1))
        )
        batch.commit().await()
    }

    suspend fun create(user: User) {
        firestore.users
            .document(user.id)
            .set(mapOf(User::activeSubscriptions.name to 0))
            .await()
    }

}

val FirebaseFirestore.users get() =
    collection("users")

fun FirebaseFirestore.subscriptions(userId: String) =
    users.document(userId).collection("subscriptions")