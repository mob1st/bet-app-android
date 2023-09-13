package br.com.mob1st.core.state.contracts

/**
 * Manages the selection of items in a list.
 */
interface ListSelectionManager {

    /**
     * Selects the item at the given position.
     */
    fun selectItem(position: Int)

    /**
     * Consumes the side-effect generated when the navigation was triggered.
     */
    fun consumeNavigation()

    /**
     * Restores the state of the selection manager after a failure.
     */
    fun restoreFromFailure()
}
