package br.com.mob1st.core.state.managers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.transformLatest
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Common error handler to be used by managers such as ViewModels.
 * It implements [CoroutineExceptionHandler] to catch errors in coroutines but also handle errors in a standalone way.
 * through the [catch] method.
 */
open class ErrorHandler : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(
        context: CoroutineContext,
        exception: Throwable,
    ) {
        catch(exception)
    }

    open fun catch(throwable: Throwable) {
        Timber.e(throwable)
    }
}

/**
 * Transforms a [Flow] of [T] into a [Flow] of [Result] of [T].
 * It's useful to be exposed for operations that cannot crash but at the same time should expose the failure.
 *
 * @param T the type of the [Flow]
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<T>.mapCatching(
    errorHandler: ErrorHandler = ErrorHandler(),
    map: (value: T) -> R,
): Flow<R> =
    map {
        Result.success(it)
    }.catch { throwable ->
        emit(Result.failure(throwable))
    }.transformLatest { result ->
        result
            .onSuccess { value ->
                emit(map(value))
            }
            .onFailure { throwable ->
                errorHandler.catch(throwable)
            }
    }

/**
 * Catches errors in a [Flow] and handles them with the [ErrorHandler].
 * @param T the type of the [Flow].
 * @param errorHandler the error handler to be used.
 * @return the [Flow] with the error handling.
 */
fun <T> Flow<T>.catchIn(errorHandler: ErrorHandler): Flow<T> = map { Result.success(it) }
    .catch { throwable -> errorHandler.catch(throwable) }
    .mapNotNull { it.getOrNull() }

/**
 * Catches errors in a [Flow] and handles them with the [ErrorHandler].
 * @param T the type of the [Flow].
 * @param block the block to be executed that can throw an exception.
 * @return the [Flow] with the error handling.
 */
inline fun ErrorHandler.catching(block: () -> Unit) {
    runCatching {
        block()
    }.onFailure { throwable ->
        catch(throwable)
    }
}
