package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.state.contracts.UiStateOutputManager

/**
 * Manages the UI state of the builder screen.
 */
internal object BuilderUiStateManager {
    /**
     * Represents the UI state of the builder screen.
     */
    interface Output : UiStateOutputManager<BuilderUiState>

    /**
     * Captures the user input events of the builder screen.
     */
    interface Input {
        /**
         * It can be called when the user clicks on a manual category.
         * @param position The position of the manual category.
         */
        fun selectManualCategory(position: Int)

        /**
         * It can be called when the user clicks on a suggested category.
         * @param position The position of the suggested category.
         */
        fun selectSuggestedCategory(position: Int)

        /**
         * Sets the category name according to the user input.
         * @param name The name of the category.
         */
        fun setCategoryName(name: String)

        /**
         * Called when the user clicks on the submit button of the dialog.
         */
        fun submitManualCategoryName()

        /**
         * Save the user inputs.
         */
        fun save()
    }
}
