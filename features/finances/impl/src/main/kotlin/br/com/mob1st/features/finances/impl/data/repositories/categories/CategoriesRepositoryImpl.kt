package br.com.mob1st.features.finances.impl.data.repositories.categories

import br.com.mob1st.core.data.suspendTransaction
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.CategoriesQueries
import br.com.mob1st.features.finances.impl.data.morphisms.CategoryDataMapper
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of the [CategoriesRepository] interface.
 * @property io The IO dispatcher.
 * @property queries The queries for the categories.
 * @property categoryDataMapper The mapper for the category data.
 */
internal class CategoriesRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val queries: CategoriesQueries,
    private val categoryDataMapper: CategoryDataMapper,
) : CategoriesRepository {
    override fun getManuallyCreatedBy(
        step: BuilderNextAction.Step,
    ): Flow<List<Category>> {
        return queries
            .selectManuallyCreatedCategories(io, step.isExpense)
            .map { query ->
                categoryDataMapper.map(step.type, query)
            }
    }

    override suspend fun add(
        category: Category,
        linkedSuggestion: CategorySuggestion?,
    ) = queries.suspendTransaction(io) {
        queries.insertCategories(category, linkedSuggestion)
        queries.insertRecurrences(category.id, category.recurrences)
    }

    override suspend fun set(category: Category) = withContext(io) {
        queries.update(category)
    }

    override suspend fun delete(category: Category) = withContext(io) {
        queries.delete(category)
    }

    override suspend fun deleteRecurrence(category: Category, recurrenceIndex: Int) = withContext(io) {
        queries.deleteRecurrence(category, recurrenceIndex)
    }
}
