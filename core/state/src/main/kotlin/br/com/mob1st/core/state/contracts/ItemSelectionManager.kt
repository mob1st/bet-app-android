package br.com.mob1st.core.state.contracts

/**
 * A standard interface for ViewModels that a selection from a item in a list.
 */
interface ItemSelectionManager<T> {

    /**
     * Handle a selection from a item in a list.
     */
    fun selected(item: T)
}
