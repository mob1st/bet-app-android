package br.com.mob1st.features.finances.impl.data.repositories.categories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import br.com.mob1st.core.data.suspendTransaction
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
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
 * @property db The SqlDelight database instance.
 * @property selectCategoryViewMapper The mapper for the select category view to the domain entity.
 * @property deleteRecurrenceMapper The mapper for the delete recurrence query.
 */
internal class CategoriesRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val db: TwoCentsDb,
    private val selectCategoryViewMapper: SelectCategoryViewsMapper,
    private val deleteRecurrenceMapper: DeleteRecurrenceMapper,
) : CategoriesRepository {
    override fun getManuallyCreatedBy(
        step: BuilderNextAction.Step,
    ): Flow<List<Category>> {
        return db.categoriesQueries
            .selectManuallyCreatedCategories(step.isExpense)
            .asFlow()
            .mapToList(io)
            .map { query ->
                selectCategoryViewMapper.map(step.type, query)
            }
    }

    override suspend fun add(
        category: Category,
        linkedSuggestion: CategorySuggestion?,
    ) = db.categoriesQueries.suspendTransaction(io) {
        db.categoriesQueries.insertCategories(category, linkedSuggestion)
        val categoryId = db.commonsQueries.lastInsertRowId().executeAsOne()
        db.categoriesQueries.insertRecurrences(
            id = categoryId,
            recurrences = category.recurrences,
        )
    }

    override suspend fun set(category: Category) = withContext(io) {
        db.categoriesQueries.updateCategory(
            id = category.id.value,
            name = category.name,
            amount = category.amount.cents,
        )
    }

    override suspend fun delete(category: Category) = withContext(io) {
        db.categoriesQueries.deleteCategory(category.id.value)
    }

    override suspend fun deleteRecurrence(category: Category, recurrenceIndex: Int) = withContext(io) {
        val (p1, p0) = deleteRecurrenceMapper.map(category.recurrences, recurrenceIndex)
        db.categoriesQueries.deleteRecurrence(category.id.value, p1, p0)
    }
}
