package br.com.mob1st.core.state.contracts

/**
 * A standard interface for ViewModels that handle pagination in the UI.
 */
interface PaginationManager {
    /**
     * Paginates to the next page.
     */
    fun next()
}
