package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.entities.toDefaultRecurrences
import br.com.mob1st.features.finances.impl.domain.infra.repositories.AssetsRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetCategoryDetailUseCase(
    private val categoryRepository: CategoriesRepository,
    private val assetsRepository: AssetsRepository,
) {
    operator fun get(intent: GetCategoryIntent): Flow<Category> {
        return when (intent) {
            is GetCategoryIntent.Edit -> categoryRepository.getById(intent.id)
            is GetCategoryIntent.Create -> assetsRepository.getByTag(intent.name).map {
                val image = it.first()
                Category(
                    name = intent.name,
                    image = image,
                    isExpense = intent.isExpense,
                    recurrences = intent.type.toDefaultRecurrences(),
                    isSuggested = false,
                )
            }
        }
    }
}
