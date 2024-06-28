package br.com.mob1st.features.finances.impl.domain.repositories

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import kotlinx.coroutines.flow.Flow

/**
 * Repository for the Categories aggregate.
 */
internal interface CategoriesRepository {
    /**
     * Returns the categories that were manually created by the user in the given [step].
     * @param step The step of the category builder.
     * @return A flow of the categories.
     */
    fun getManuallyCreatedBy(
        step: BuilderNextAction.Step,
    ): Flow<List<Category>>

    /**
     * Adds a new category into the user's list of categories.
     * It can be either a category added manually by the user or a category that was suggested by the system.
     * @param category The category to be added.
     * @param linkedSuggestion The suggestion that was linked to the category, if any.
     */
    suspend fun add(
        category: Category,
        linkedSuggestion: CategorySuggestion?,
    )

    /**
     * Updates the given category.
     */
    suspend fun set(category: Category)

    /**
     * Deletes the given category.
     * @param category The category to be deleted.
     */
    suspend fun delete(category: Category)

    /**
     * Delete a recurrence in a specific position in the recurrence list of a category.
     * It can be used to remove a day in a month for a monthly recurrence, for example.
     * @param category The category to have the recurrence removed.
     * @param recurrenceIndex The index of the recurrence to be removed.
     */
    suspend fun deleteRecurrence(
        category: Category,
        recurrenceIndex: Int,
    )
}
