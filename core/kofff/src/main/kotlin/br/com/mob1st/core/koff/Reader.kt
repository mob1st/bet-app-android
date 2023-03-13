package br.com.mob1st.core.koff

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlin.math.pow

/**
 * Reads from cache and network, if cache is not updated.
 *
 * @param readFromCache The operation to read from cache.
 * @param readFromNetwork The operation to read from network.
 * @param isCacheUpdated The operation to check if cache is updated. True if cache is updated, false otherwise.
 * @return The flow of data from cache.
 */
fun <T> readFromCacheAndNetwork(
    readFromCache: () -> Flow<T>,
    readFromNetwork: suspend () -> Unit,
    isCacheUpdated: suspend () -> Boolean
): Flow<T> {
    return flow {
        if (!isCacheUpdated()) {
            readFromNetwork()
        }
        emitAll(readFromCache())
    }
}

fun <T> Flow<T>.exponencialBackoff(
    initialDelay: Float = 0.0f,
    retryFactor: Float = 1.0f,
    predicate: suspend FlowCollector<T>.(cause: Throwable, attempt: Long, delay: Long) -> Boolean
): Flow<T> = retryWhen { cause, attempt ->
    val retryDelay = initialDelay * retryFactor.pow(attempt.toFloat())
    predicate(cause, attempt, retryDelay.toLong())
}
