package br.com.mob1st.bet.features.bet

import kotlinx.coroutines.flow.Flow
import java.util.Date

interface BetRepository {

    fun get(from: Date): Flow<List<Guess>>

}