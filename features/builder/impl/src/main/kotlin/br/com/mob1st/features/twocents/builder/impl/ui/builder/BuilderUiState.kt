package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Ui state for the category builder
 * @property manuallyAdded The manually added categories.
 * @property suggested The suggested categories.
 * @property keyboard The numeric keyboard state.
 * @property dialog The category name dialog state.
 */
@Immutable
internal data class BuilderUiState(
    val manuallyAdded: ImmutableList<ListItem> = persistentListOf(),
    val suggested: ImmutableList<ListItem> = persistentListOf(),
    val keyboard: CategorySheet? = null,
    val dialog: CategoryNameDialog? = null,
) {
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
