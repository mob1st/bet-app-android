package br.com.mob1st.core.state.managers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Common error handler to be used by managers such as ViewModels.
 * It implements [CoroutineExceptionHandler] to catch errors in coroutines but also handle errors in a standalone way.
 * through the [catch] method.
 * @property onError the error handler to be called when an error occurs.
 */
class ErrorHandler(
    private val onError: (Throwable) -> Unit,
) : CoroutineExceptionHandler {
    override fun handleException(
        context: CoroutineContext,
        exception: Throwable,
    ) {
        catch(exception)
    }

    /**
     * Handles the error.
     * @param throwable the error to be handled.
     */
    fun catch(throwable: Throwable) {
        Timber.e(throwable)
        onError(throwable)
    }

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler
}

/**
 * Transforms a [Flow] of [T] into a [Flow] of [Result] of [T].
 * It's useful to be exposed for operations that cannot crash but at the same time should expose the failure.
 *
 * @param T the type of the [Flow]
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<T>.mapCatching(
    map: (value: T) -> R,
    errorHandler: ErrorHandler,
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

fun <T> Flow<T>.catchIn(errorHandler: ErrorHandler): Flow<T> = catch { throwable -> errorHandler.catch(throwable) }
