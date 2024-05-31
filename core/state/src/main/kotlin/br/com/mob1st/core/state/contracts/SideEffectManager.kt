package br.com.mob1st.core.state.contracts

import br.com.mob1st.morpheus.annotation.Consumable

/**
 * A standard interface for ViewModels that consume side effects produced by the UI.
 * It works on top of the [Consumable] class, available by morpheus annotation library.
 */
interface SideEffectManager<T> {
    /**
     * Consume a side effect produced by the UI.
     *
     * This method is usually called after an error message dismiss or a navigation action.
     */
    fun consume(consumable: Consumable<T, *>)
}
