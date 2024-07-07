package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.events.BuilderStepScreenViewFactory
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.SuggestionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

internal class GetCategoryBuilderUseCase(
    private val analyticsReporter: AnalyticsReporter,
    private val categoryRepository: CategoriesRepository,
    private val suggestionsRepository: SuggestionsRepository,
    private val builderStepScreenViewFactory: BuilderStepScreenViewFactory,
) {
    operator fun get(
        step: BuilderNextAction.Step,
    ): Flow<BudgetBuilder> {
        return combine(
            categoryRepository.getManuallyCreatedBy(step),
            suggestionsRepository.getByStep(step),
        ) { manuallyAdded, suggestions ->
            BudgetBuilder(
                id = step,
                manuallyAdded = manuallyAdded,
                suggestions = suggestions,
            )
        }.onStart {
            analyticsReporter.log(builderStepScreenViewFactory.create(step))
        }
    }
}
