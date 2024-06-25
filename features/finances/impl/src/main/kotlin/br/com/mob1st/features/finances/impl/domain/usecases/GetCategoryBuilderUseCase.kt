package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategoryBuilder
import br.com.mob1st.features.finances.impl.domain.entities.toParameter
import br.com.mob1st.features.finances.impl.domain.events.BuilderStepScreenViewFactory
import br.com.mob1st.features.finances.impl.domain.repositories.CategoryRepository
import br.com.mob1st.features.finances.impl.domain.repositories.SuggestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

internal class GetCategoryBuilderUseCase(
    private val analyticsReporter: AnalyticsReporter,
    private val categoryRepository: CategoryRepository,
    private val suggestionsRepository: SuggestionRepository,
    private val builderStepScreenViewFactory: BuilderStepScreenViewFactory,
) {
    operator fun get(
        step: BuilderNextAction.Step,
    ): Flow<CategoryBuilder> {
        val (categoryType, isExpense) = step.toParameter()
        return combine(
            categoryRepository.getManuallyCreatedBy(categoryType, isExpense),
            suggestionsRepository.getByTypeAndIsExpense(categoryType, isExpense),
        ) { manuallyAdded, suggestions ->
            CategoryBuilder(
                step = step,
                manuallyAdded = manuallyAdded,
                suggestions = suggestions,
            )
        }.onStart {
            analyticsReporter.log(builderStepScreenViewFactory.create(step))
        }
    }
}
