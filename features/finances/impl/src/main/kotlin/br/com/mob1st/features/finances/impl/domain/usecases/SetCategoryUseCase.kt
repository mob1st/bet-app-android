package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository

internal class SetCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val analyticsReporter: AnalyticsReporter,
) {
    suspend operator fun invoke(
        category: Category,
    ) {
        categoryRepository.set(category)
        analyticsReporter.report(AnalyticsEvent.categorySent(category))
    }

    private fun AnalyticsEvent.Companion.categorySent(
        category: Category,
    ) = AnalyticsEvent(
        name = "category_sent",
        params = mapOf(
            "category_id" to category.id,
            "category_name" to category.name,
            "amount" to category.amount.cents,
            category.recurrences.keyValue(),
        ),
    )

    private fun Recurrences.keyValue() = when (this) {
        is Recurrences.Fixed -> "fixed" to day.value
        Recurrences.Variable -> "variable" to "weekly"
        is Recurrences.Seasonal -> "seasonal" to daysOfYear.map { it.value }.joinToString { "," }
    }
}
