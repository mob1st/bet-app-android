package br.com.mob1st.bet.features.bet

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory
import java.util.Date

@Factory(binds = [BetRepository::class])
class BetRepositoryImpl(
    private val firestore: FirebaseFirestore
) : BetRepository{
    override fun get(from: Date): Flow<List<Guess>> {
        return flow {
            val bets = firestore.collection(BetSchema.COLLECTION_NAME)
                .get()
                .await()
                .documents
                .map { documentSnapshot ->
                    documentSnapshot.toBet()
                }
            emit(bets)
        }
    }
}

private object BetSchema {
    const val COLLECTION_NAME = "bet"
}

fun DocumentSnapshot.toBet(): Guess {
    TODO()
}