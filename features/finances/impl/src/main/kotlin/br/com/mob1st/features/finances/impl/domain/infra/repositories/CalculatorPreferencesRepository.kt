package br.com.mob1st.features.finances.impl.domain.infra.repositories

import br.com.mob1st.features.finances.impl.domain.entities.CalculatorPreferences
import kotlinx.coroutines.flow.Flow

interface CalculatorPreferencesRepository {
    fun get(): Flow<CalculatorPreferences>

    suspend fun set(preferences: CalculatorPreferences)
}
