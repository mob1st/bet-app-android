package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.data.asFlowListEach
import br.com.mob1st.core.data.asFlowSingle
import br.com.mob1st.core.data.suspendTransaction
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of the [CategoriesRepository] interface.
 * @property io The IO dispatcher.
 * @property db The SqlDelight database instance.
 * @property categoriesDataMap Maps the queries that return categories to the domain entities.
 */
internal class CategoryRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val db: TwoCentsDb,
    private val categoriesDataMap: CategoriesDataMap,
) : CategoriesRepository {
    override fun getByIsExpenseAndRecurrencesType(
        isExpense: Boolean,
        recurrenceType: RecurrenceType,
    ): Flow<List<Category>> {
        return db.categoriesQueries
            .selectCategoriesByRecurrenceType(
                is_expense = isExpense,
                recurrence_type = RecurrenceColumns.rawTypeFrom(recurrenceType),
            )
            .asFlowListEach(io) { categories ->
                categoriesDataMap(categories)
            }
    }

    override fun countByIsExpenseAndRecurrencesType(
        isExpense: Boolean,
        recurrenceType: RecurrenceType,
    ): Flow<Long> {
        return db.categoriesQueries
            .countCategoriesByRecurrenceType(
                is_expense = isExpense,
                recurrence_type = RecurrenceColumns.rawTypeFrom(recurrenceType),
            )
            .asFlowSingle(io) { it }
    }

    override fun getById(id: Category.Id): Flow<Category> {
        return db.categoriesQueries
            .selectCategoryById(id.value)
            .asFlowSingle(io) { categories ->
                categoriesDataMap(categories)
            }
    }

    override suspend fun addAll(categories: List<Category>) = db.suspendTransaction(io) {
        categories.forEach { category ->
            db.categoriesQueries.insert(category)
        }
    }

    override suspend fun add(
        category: Category,
    ) = withContext(io) {
        db.categoriesQueries.insert(category)
    }

    override suspend fun set(category: Category) = withContext(io) {
        db.categoriesQueries.update(category)
    }

    override suspend fun remove(category: Category) = withContext(io) {
        db.categoriesQueries.deleteCategory(category.id.value)
    }
}
