package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import br.com.mob1st.core.data.suspendTransaction
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of the [CategoriesRepository] interface.
 * @property io The IO dispatcher.
 * @property db The SqlDelight database instance.
 */
internal class CategoriesRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val db: TwoCentsDb,
) : CategoriesRepository {
    override fun getByStep(step: BuilderNextAction.Step): Flow<List<Category>> {
        val args = SelectCategoriesByStepArgs.from(step)
        return db.categoriesQueries
            .selectCategoriesByRecurrenceType(
                is_expense = args.isExpense,
                recurrence_type = args.recurrenceTypeDescription,
            )
            .asFlow()
            .mapToList(io)
            .map { categoriesList ->
                categoriesList.map { categories ->
                    categories.toDomain()
                }
            }
    }

    override fun getById(id: Category.Id): Flow<Category> {
        return db.categoriesQueries
            .selectCategoryById(id.value)
            .asFlow()
            .mapToOne(io)
            .map { categories ->
                categories.toDomain()
            }
    }

    override suspend fun addAll(categories: List<Category>) = db.suspendTransaction(io) {
        categories.forEach { category ->
            db.categoriesQueries.insert(category)
        }
    }

    override suspend fun add(
        category: Category,
    ) = db.suspendTransaction(io) {
        db.categoriesQueries.insert(category)
    }

    override suspend fun set(category: Category) = db.suspendTransaction(io) {
        db.categoriesQueries.update(category)
    }

    override suspend fun remove(category: Category) = withContext(io) {
        db.categoriesQueries.deleteCategory(category.id.value)
    }
}
