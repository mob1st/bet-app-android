package br.com.mob1st.core.state.contracts

/**
 * A standard interface for UiStates that can triggers navigation to other screens
 */
interface Navigable<T> {

    val navTarget: T?
}
