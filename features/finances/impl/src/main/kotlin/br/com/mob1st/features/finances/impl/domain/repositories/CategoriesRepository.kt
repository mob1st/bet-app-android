package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import kotlinx.coroutines.flow.Flow

/**
 * Repository for the Categories aggregate.
 */
internal interface CategoriesRepository {
    /**
     * Return a single category by its [id].
     * @param id The id of the category.
     * @return A flow of the category.
     */
    fun getById(
        id: Category.Id,
    ): Flow<Category>

    /**
     * Return all categories related to the given [step].
     * @param step The step of the category builder.
     * @return A flow of the categories.
     */
    fun getByStep(
        step: BuilderNextAction.Step,
    ): Flow<List<Category>>

    /**
     * Bulk inserts the given [categories].
     * @param categories The categories to be inserted.
     */
    suspend fun addAll(categories: List<Category>)

    /**
     * Adds a new category into the user's list of categories.
     * @param category The category to be added.
     */
    suspend fun add(category: Category)

    /**
     * Updates the given category.
     */
    suspend fun set(category: Category)

    /**
     * Removes the given category from the user's list of categories.
     * @param category The category to be deleted.
     */
    suspend fun remove(category: Category)
}
