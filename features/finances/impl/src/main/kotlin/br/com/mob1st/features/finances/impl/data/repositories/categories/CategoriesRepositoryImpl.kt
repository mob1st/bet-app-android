package br.com.mob1st.features.finances.impl.data.repositories.categories

import br.com.mob1st.core.data.suspendTransaction
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.data.morphisms.ListCategoryViewMapper
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.repositories.CategoryRepository
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class CategoriesRepositoryImpl(
    private val db: TwoCentsDb,
    private val io: IoCoroutineDispatcher,
    private val categoryDataMapper: ListCategoryViewMapper,
) : CategoryRepository {
    private val queries by lazy { db.categoriesQueries }

    override fun getManuallyCreatedBy(
        type: CategoryType,
        isExpense: Boolean,
    ): Flow<List<Category>> {
        return queries.selectManuallyCreatedCategories(io, isExpense) { query ->
            categoryDataMapper.map(type, query)
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
