package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.events.BuilderStepScreenViewFactory
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal class GetCategoryBuilderUseCase(
    private val analyticsReporter: AnalyticsReporter,
    private val categoryRepository: CategoriesRepository,
    private val screenViewFactory: BuilderStepScreenViewFactory,
) {
    operator fun get(
        step: BuilderNextAction.Step,
    ): Flow<BudgetBuilder> {
        return categoryRepository.getByIsExpenseAndRecurrencesType(
            isExpense = step.isExpense,
            recurrenceType = step.type,
        ).map { categories ->
            BudgetBuilder(step, categories)
        }.onStart {
            analyticsReporter.log(screenViewFactory.create(step))
        }
    }
}
