package br.com.mob1st.core.state.contracts

/**
 * Manages the selection of items in a list.
 */
interface ListSelectionManager {
    /**
     * Selects the item at the given position.
     */
    fun selectItem(position: Int)
}
