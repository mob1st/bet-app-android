package br.com.mob1st.bet.features.bet

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.firebase.all
import br.com.mob1st.bet.core.utils.PageFilter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import java.util.Date

@Factory
class MatchRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val dispatcherProvider: DispatcherProvider,
) : MatchRepository {

    private val io get() = dispatcherProvider.io

    override suspend fun getFootballMatches(
        date: Date,
        round: Round,
        pageFilter: PageFilter
    ) = withContext(io) {
        runCatching {
            matchCollection()
                .whereGreaterThanOrEqualTo("date", date)
                .whereEqualTo("round.id", round.id)
                .whereLessThan(" ", "")
                .orderBy("date", Query.Direction.ASCENDING)
                .startAt(pageFilter.page)
                .limit(pageFilter.limit)
                .all<Match<FootballBet>>()
        }.getOrElse {
            throw GetMatchesException(it, date = date)
        }
    }

    private fun matchCollection(): CollectionReference {
        return firestore.collection("football")
            .document("world_cup_2022")
            .collection("match")
    }

}