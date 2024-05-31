package br.com.mob1st.core.state.contracts

/**
 * A dummy way to handle clicks on main button of helpers components.
 *
 * This is typically used for generic error messages displayed in helper components.
 * Does not use this interface when a UI can present many different helpers.
 */
interface HelperClickManager {
    /**
     * Handle a click on main button in the UI.
     */
    fun helperPrimaryAction()
}
