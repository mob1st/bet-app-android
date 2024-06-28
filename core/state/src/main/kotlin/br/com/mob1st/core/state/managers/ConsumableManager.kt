package br.com.mob1st.core.state.managers

import arrow.optics.Lens
import arrow.optics.copy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Allows the UI to consume side effects changes emitted by the ViewModel.
 * It depends on Arrow's Optics to access the properties of the State and determine which side effects to consume.
 * @param State The of the consumable that holds the side effects.
 */
interface ConsumableManager<State> {
    /**
     * The state flow that holds the consumable side effects.
     */
    val consumableUiState: StateFlow<State>

    /**
     * Consumes the side effect of the given [lens].
     * It sets the property to null.
     * @param lens The lens that points to the property to consume.
     */
    fun <Property : Any?> consume(
        lens: Lens<State, Property?>,
    )
}

/**
 * A delegate that implements the [ConsumableManager] interface.
 * It uses a [MutableStateFlow] to hold the consumable side effects.
 * @param T The type of the consumable state.
 * @param initialValue The initial value of the consumable state.
 */
class ConsumableDelegate<T : Any>(
    initialValue: T,
) : ConsumableManager<T>,
    MutableStateFlow<T> by MutableStateFlow(initialValue) {
    override val consumableUiState: StateFlow<T> = asStateFlow()

    override fun <Property> consume(lens: Lens<T, Property?>) {
        update {
            it.copy { lens set null }
        }
    }
}
