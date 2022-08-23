package br.com.mob1st.bet.features.bet

import br.com.mob1st.bet.core.logs.Debuggable
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory
import java.util.Date

@Factory
class MatchRepositoryImpl(
    private val firestore: FirebaseFirestore
) : MatchRepository {

    override suspend fun getFootballMatches(): List<Match<FootballBet>> {
        val querySnapshot = runCatching {
            firestore.collection("football")
                .document("world_cup_2022")
                .collection("match")
                .get()
                .await()
        }.getOrElse {
            throw GetMatchesException(it, date = Date())
        }
        TODO()
    }
}

class GetMatchesException(
    cause: Throwable,
    private val date: Date
) : Exception("unable to fetch the matches", cause), Debuggable {
    override fun logProperties(): Map<String, Any> {
        return mapOf(
            "date" to date
        )
    }
}