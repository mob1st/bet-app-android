package br.com.mob1st.core.state.managers

import arrow.optics.Lens

interface ConsumableManager<State> {
    fun <Property : Any?> consume(
        lens: Lens<State, Property?>,
    )
}
