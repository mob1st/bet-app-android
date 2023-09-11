package br.com.mob1st.features.utils.errors

import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class LogExceptionHandler(
    private val extraHandler: ((exception: Throwable) -> Unit)? = null,
) : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler.Key
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Timber.e(exception)
        extraHandler?.let {
            it.invoke(exception)
        }
    }
}

/**
 * Common exception handler that avoid crashes, log the errors and convert them into [CommonError]s.
 * It's a common handler for all features to show the basic ui messages on UI, but also allowing error customizaton
 * through the [Catch] interface.
 * Always use it as default error handler for all opened coroutines
 * @param generalHandler is a lambda that will be called when an exception is thrown.
 * @see Catch
 */
class CommonExceptionHandler(
    private val generalHandler: (CommonError) -> Unit,
) : CoroutineExceptionHandler {

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler.Key
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Timber.e(exception)
        generalHandler(CommonError.from(exception))
    }

    /**
     * This operator allows the addition of a custom error handler for a specific exception.
     * If the exception is not the one specified, the default error handler will be called.
     * The log will be printed in both cases.
     *
     * Example
     * ```
     * val baseHandler = MessageExceptionHandler {...}
     * val coroutineExceptionHandler = baseHandler + AdditionalCatching<OtherException> {...}
     * launch(courseExceptionHandler) {...}
     * ```
     * @param T is the type of the exception to be caught
     * @param catch is the lambda that will be called when the exception is thrown.
     * @see Catch
     */
    inline operator fun <reified T : Exception> plus(catch: Catch<T>) =
        CoroutineExceptionHandler { context, exception ->
            if (exception is T) {
                Timber.e(exception)
                catch(exception)
            } else {
                this@CommonExceptionHandler.handleException(context, exception = exception)
            }
        }
}

/**
 * This interface allows the addition of a custom error handler for a specific exception.
 * @see CommonExceptionHandler.plus
 */
fun interface Catch<T : Throwable> {

    /**
     * Handles the exception
     */
    operator fun invoke(exception: T)
}
