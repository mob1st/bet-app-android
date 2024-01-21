package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.completeFixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.getFilled
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrentCategoryRepository
import kotlinx.coroutines.flow.first

internal class CompleteFixedExpansesStepUseCase(
    private val recurrenceBuilderRepository: RecurrenceBuilderRepository,
    private val recurrentCategoryRepository: RecurrentCategoryRepository,
    private val analyticsReporter: AnalyticsReporter,
) {

    suspend operator fun invoke() {
        val builder = recurrenceBuilderRepository.get().first()
        val newBuilder = builder.completeFixedExpensesStep()
        val filledRecurrences = newBuilder.fixedExpensesStep.getFilled()
        recurrentCategoryRepository.put(filledRecurrences)
        recurrenceBuilderRepository.set(newBuilder)
        analyticsReporter.log(
            ProceedInBuilderEvent(
                stepName = "fixed",
                addedCount = filledRecurrences.size
            )
        )
    }

    private data class ProceedInBuilderEvent(
        val stepName: String,
        val addedCount: Int,
    ) : AnalyticsEvent {
        override val name: String = "fin_builder_${stepName}_completed"
        override val logInfo: Map<String, Any?> = mapOf(
            "added" to addedCount
        )
    }
}
