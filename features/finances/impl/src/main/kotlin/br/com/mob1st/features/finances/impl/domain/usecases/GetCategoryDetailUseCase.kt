package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategoryDetail
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.toDefaultRecurrences
import br.com.mob1st.features.finances.impl.domain.infra.repositories.AssetRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CalculatorPreferencesRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

internal class GetCategoryDetailUseCase(
    private val categoryRepository: CategoryRepository,
    private val calculatorPreferencesRepository: CalculatorPreferencesRepository,
    private val assetRepository: AssetRepository,
) {
    operator fun get(intent: GetCategoryIntent): Flow<CategoryDetail> {
        val categoryFlow = when (intent) {
            is GetCategoryIntent.Edit -> categoryRepository.getById(intent.id)
            is GetCategoryIntent.Create -> assetRepository.getByTag(intent.name).map {
                val asset = it.first()
                Category(
                    name = intent.name,
                    image = asset.uri,
                    isExpense = intent.defaultValues.isExpense,
                    recurrences = intent.defaultValues.recurrenceType.toDefaultRecurrences(),
                    isSuggested = false,
                )
            }
        }
        return combine(
            categoryFlow,
            calculatorPreferencesRepository.get(),
            ::CategoryDetail,
        )
    }
}
