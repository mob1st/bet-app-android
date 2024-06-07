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
         * Adds a manual category.
         * It can be called when the user clicks on the add Manual Category button.
         */
        fun addManualCategory()

        /**
         * Updates a manual category.
         */
        fun updateManualCategory(
            position: Int,
            name: String,
            value: String,
        )

        /**
         * Updates the value of a suggested category.
         */
        fun updateSuggestedCategory(
            position: Int,
            value: String,
        )

        /**
         * Save the user inputs.
         */
        fun save()
    }
}
