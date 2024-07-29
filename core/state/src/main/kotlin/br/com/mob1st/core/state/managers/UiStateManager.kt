package br.com.mob1st.core.state.managers

import kotlinx.coroutines.flow.StateFlow

/**
 * A common interface for ViewModels expose the ui state in a standard way.
 */
interface UiStateManager<T> {
    /**
     * The current state of the UI.
     *
     * Create a data class and use it as T.
     * This data class should represent the entire state of the UI, including side effects such as error messages and
     * navigation actions.
     */
    val uiState: StateFlow<T>
}
