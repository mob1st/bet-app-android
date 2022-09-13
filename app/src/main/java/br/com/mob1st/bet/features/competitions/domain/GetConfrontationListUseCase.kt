package br.com.mob1st.bet.features.competitions.domain

import kotlinx.coroutines.delay
import java.util.Date

class GetConfrontationListUseCase {

    suspend operator fun invoke(): List<Confrontation> {
        delay(1_000)
        return emptyList()
    }

}

data class Confrontation(val data: Date, val team1: String, val team2: String)