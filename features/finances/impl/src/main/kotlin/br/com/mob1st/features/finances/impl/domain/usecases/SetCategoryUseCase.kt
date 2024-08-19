package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.events.SetCategoryEventFactory
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository

internal class SetCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val analyticsReporter: AnalyticsReporter,
    private val eventFactory: SetCategoryEventFactory,
) {
    suspend operator fun invoke(category: Category) {
        if (category.id.isWritten()) {
            categoryRepository.set(category)
        } else {
            categoryRepository.add(category)
        }
        val event = eventFactory.create(category)
        analyticsReporter.report(event)
    }
}
