package br.com.mob1st.core.state.contracts

/**
 * A standard interface for ViewModels that can be refreshed the UI state.
 */
interface RefreshManager {
    /**
     * Refresh the UI state.
     */
    fun refresh()
}
