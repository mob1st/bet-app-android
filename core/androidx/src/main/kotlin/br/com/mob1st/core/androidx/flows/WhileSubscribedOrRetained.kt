package br.com.mob1st.core.androidx.flows

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val ANR_TIMEOUT = 5_000L

/**
 * Default sharing strategy for a flow that will be retained.
 * It uses the [SharingStarted.WhileSubscribed] strategy using the ANR timeout to decide if the flow should be
 * restarted when there is no subscribers.
 */
fun <T> Flow<T>.stateInWhileSubscribed(
    scope: CoroutineScope,
    initialValue: T,
): StateFlow<T> =
    stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(ANR_TIMEOUT),
        initialValue = initialValue,
    )
