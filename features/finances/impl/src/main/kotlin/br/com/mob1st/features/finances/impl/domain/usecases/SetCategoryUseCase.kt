package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository

internal class SetCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val analyticsReporter: AnalyticsReporter,
) {
    suspend operator fun invoke(
        category: Category,
    ) {
        categoryRepository.set(category)
        TODO()
    }
}