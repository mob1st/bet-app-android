package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrenceType
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
     * Returns the list of categories that matches the given parameters [isExpense] and [recurrenceType].
     * @param isExpense Whether the category is an expense or an income.
     * @param recurrenceType The type of recurrence of the category.
     * @return A flow of the categories.
     */
    fun getByIsExpenseAndRecurrencesType(
        isExpense: Boolean,
        recurrenceType: RecurrenceType,
    ): Flow<List<Category>>

    /**
     * Counts the number of categories that matches the given parameters [isExpense] and [recurrenceType].
     * @param isExpense Whether the category is an expense or an income.
     * @param recurrenceType The type of recurrence of the category.
     * @return The number of categories.
     */
    fun countByIsExpenseAndRecurrencesType(
        isExpense: Boolean,
        recurrenceType: RecurrenceType,
    ): Long

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
