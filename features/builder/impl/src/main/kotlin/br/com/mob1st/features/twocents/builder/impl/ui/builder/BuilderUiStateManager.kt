package br.com.mob1st.features.twocents.builder.impl.ui.builder

import br.com.mob1st.core.state.contracts.UiStateOutputManager

/**
 * Manages the UI state of the builder screen.
 */
internal interface BuilderUiStateManager : UiStateOutputManager<BuilderUiState> {
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
     * Save the user inputs.
     */
    fun save()
}
