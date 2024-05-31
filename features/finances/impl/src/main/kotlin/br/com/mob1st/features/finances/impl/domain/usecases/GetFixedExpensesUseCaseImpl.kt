package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.core.observability.events.ScreenViewEvent
import br.com.mob1st.features.finances.impl.domain.entities.getBudgetItemGroup
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItemGroup
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.usecases.GetFixedExpensesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Get the step content for the field selected in the builder
 */
internal class GetFixedExpensesUseCaseImpl(
    private val recurrenceBuilderRepository: RecurrenceBuilderRepository,
    private val analyticsReporter: AnalyticsReporter,
) : GetFixedExpensesUseCase {
    override operator fun invoke(): Flow<BudgetItemGroup<RecurrentCategory>> {
        return recurrenceBuilderRepository
            .getBudgetItemGroup {
                it.fixedExpensesStep
            }
            .onStart {
                analyticsReporter.log(
                    ScreenViewEvent("fin_builder_fixed_expenses"),
                )
            }
    }
}
