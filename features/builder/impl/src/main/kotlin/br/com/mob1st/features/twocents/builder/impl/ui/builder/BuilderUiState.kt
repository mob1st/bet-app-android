package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.twocents.builder.impl.R
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategorySuggestion
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus

/**
 * Ui state for the category builder
 * @property manuallyAddedList The manually added categories.
 * @property suggestedSection The suggested categories.
 * @property keyboard The numeric keyboard state.
 * @property dialog The category name dialog state.
 */
@Immutable
internal data class BuilderUiState(
    private val manuallyAddedList: PersistentList<ListItem> = persistentListOf(),
    val suggestedSection: ImmutableList<ListItem> = persistentListOf(),
    val keyboard: CategorySheet? = null,
    val dialog: CategoryNameDialog? = null,
) {
    val manuallyAddedSection = manuallyAddedList + ListItem(
        name = TextState(R.string.builder_commons_custom_section_add_item),
        amount = "",
    )

    /**
     * Converts the state visible on screen to a structure that can be parcelized.
     */
    fun toSavedState(suggestions: List<CategorySuggestion>): BuilderUserInput {
        return BuilderUserInput(
            manuallyAdded = manuallyAddedList.toManualEntryList(),
            suggested = suggestedSection.toSuggestedEntryMap(suggestions),
        )
    }

    /**
     * Indicates if the given [position] is the add button or not in the manually added section.
     * @return true if this is the add button item, false otherwise
     */
    fun isAddButton(position: Int): Boolean {
        return position == manuallyAddedSection.lastIndex
    }

    /**
     * Creates a bottom sheet to update a manual added item placed in the given [position].
     */
    fun showUpdateManualSheet(position: Int) = CategorySheet.updateManual(
        position = position,
        item = manuallyAddedList[position],
    )

    /**
     * Creates a bottom sheet ot update a suggested item placed in the given [position]
     */
    fun showUpdateSuggestedSheet(position: Int) = CategorySheet.updateSuggestion(
        position = position,
        item = suggestedSection[position],
    )

    /**
     * List item state
     */
    @Immutable
    data class ListItem(
        val name: TextState,
        val amount: String = "",
    )
}

/**
 * The category name dialog state.
 * @property text The text.
 * @property isSubmitEnabled If the submit button is enabled.
 */
@Immutable
internal data class CategoryNameDialog(
    val text: String = "",
    val isSubmitEnabled: Boolean = false,
)

/**
 * Allows the number set for a category.
 * @property category The category.
 * @property amount The amount.
 */
@Immutable
internal data class CategorySheet(
    val operation: Operation,
    val category: TextState,
    val amount: String,
) {
    /**
     * Represents the operation to be performed when the sheet is submitted.
     */
    sealed interface Operation {
        /**
         * Adds a new category.
         * It's always a manual category because suggested categories are not added manually.
         */
        data object Add : Operation

        /**
         * Updates a category.
         * @property position The position of the selected category in the list.
         * @property isSuggestion If the category is a suggestion.
         */
        data class Update(
            val position: Int,
            val isSuggestion: Boolean,
        ) : Operation
    }

    companion object {
        /**
         * Creates a category sheet to update a manual category.
         */
        fun updateManual(
            position: Int,
            item: BuilderUiState.ListItem,
        ) = CategorySheet(
            operation = Operation.Update(position, isSuggestion = false),
            category = item.name,
            amount = item.amount,
        )

        /**
         * Creates a category sheet to update a suggestion.
         */
        fun updateSuggestion(
            position: Int,
            item: BuilderUiState.ListItem,
        ) = CategorySheet(
            operation = Operation.Update(position, isSuggestion = true),
            category = item.name,
            amount = item.amount,
        )

        /**
         * Creates a category sheet to add a new category.
         */
        fun add(
            name: String = "",
        ) = CategorySheet(
            operation = Operation.Add,
            category = TextState(name),
            amount = "",
        )
    }
}
