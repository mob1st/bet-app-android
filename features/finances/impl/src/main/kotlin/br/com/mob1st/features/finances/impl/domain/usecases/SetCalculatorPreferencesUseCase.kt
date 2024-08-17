package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.CalculatorPreferences
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CalculatorPreferencesRepository

class SetCalculatorPreferencesUseCase(
    private val preferencesRepository: CalculatorPreferencesRepository,
    private val analyticsReporter: AnalyticsReporter,
) {
    suspend operator fun invoke(
        calculatorPreferences: CalculatorPreferences,
    ) {
        preferencesRepository.set(calculatorPreferences)
        analyticsReporter.report(
            AnalyticsEvent.preferencesSent(calculatorPreferences),
        )
    }

    private fun AnalyticsEvent.Companion.preferencesSent(
        preferences: CalculatorPreferences,
    ) = AnalyticsEvent(
        name = "edit_calculator_preferences_sent",
        params = mapOf(
            "is_cents_enabled" to preferences.isCentsEnabled,
        ),
    )
}
