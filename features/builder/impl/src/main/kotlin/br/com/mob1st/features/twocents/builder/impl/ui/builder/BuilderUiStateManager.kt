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
         *
         */
        fun selectSuggestedCategory(position: Int)

        /**
         * Adds a manual category.
         * It can be called when the user clicks on the add Manual Category button.
         */
        fun typeManualCategoryName(name: String)

        /**
         * Called when the user clicks on the submit button of the dialog.
         */
        fun submitManualCategoryName()

        /**
         * Updates the category provided by [BuilderUiState.keyboard]
         */
        fun updateCategory()

        /**
         * Save the user inputs.
         */
        fun save()
    }
}
