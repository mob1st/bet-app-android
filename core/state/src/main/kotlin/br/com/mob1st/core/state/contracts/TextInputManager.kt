package br.com.mob1st.core.state.contracts

/**
 * A standard interface for ViewModels that handle text input in the UI.
 */
interface TextInputManager {

    /**
     * Handle a text input in the UI.
     */
    fun textInputChanged(text: String)
}
