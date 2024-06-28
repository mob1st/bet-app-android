package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Emits the value at the beginning of the flow.
 * @param T The type of the flow.
 * @param value The value to be emitted.
 * @return The flow with the value emitted at the beginning.
 */
fun <T> Flow<T>.startsWith(value: T) = onStart { emit(value) }
