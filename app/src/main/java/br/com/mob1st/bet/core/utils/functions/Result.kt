package br.com.mob1st.bet.core.utils.functions

import kotlin.coroutines.cancellation.CancellationException
import timber.log.Timber

/**
 * Attempts [block], returning a successful [Result] if it succeeds, otherwise a [Result.Failure]
 * taking care not to break structured concurrency
 */
@Suppress("TooGenericExceptionCaught")
suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Result.failure(exception)
}
