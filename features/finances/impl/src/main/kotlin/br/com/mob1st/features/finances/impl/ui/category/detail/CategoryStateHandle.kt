package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.lifecycle.SavedStateHandle
import br.com.mob1st.features.finances.impl.domain.entities.Category
import kotlinx.coroutines.flow.StateFlow

/**
 * Wrapper on top of [SavedStateHandle] to persist a [CategoryEntry] in the state across process death and retain the
 * changes made by the user in the Category before submitting it.
 * @param savedStateHandle [SavedStateHandle] to persist the state.
 */
class CategoryStateHandle(
    private val savedStateHandle: SavedStateHandle,
) {
    /**
     * Returns a [CategoryEntry] from the state. The given [category] is used to provide the initial values for the
     * entry.
     * It will emit an item everytime a new [CategoryEntry] is set through [update].
     * @param category The [Category] to provide the initial values for the entry.
     * @return A [StateFlow] with the [CategoryEntry] from the state.
     * @see update
     */
    fun entry(category: Category): StateFlow<CategoryEntry> = savedStateHandle.getStateFlow(
        ENTRY_KEY,
        CategoryEntry(category),
    )

    /**
     * Writes the given [categoryEntry] to the state bundle to persist it across process death.
     * It will trigger a new emission in the [entry] flow.
     * @param categoryEntry The [CategoryEntry] to persist.
     */
    fun update(categoryEntry: CategoryEntry) {
        savedStateHandle[ENTRY_KEY] = categoryEntry
    }

    companion object {
        private const val ENTRY_KEY = "entry_key"
    }
}
