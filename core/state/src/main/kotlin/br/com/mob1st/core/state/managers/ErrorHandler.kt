package br.com.mob1st.core.state.managers

import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Common error handler to be used by managers such as ViewModels.
 * It implements [CoroutineExceptionHandler] to catch errors in coroutines but also handle errors in a standalone way.
 * through the [handle] method.
 */
abstract class ErrorHandler : CoroutineExceptionHandler {
    final override fun handleException(
        context: CoroutineContext,
        exception: Throwable,
    ) {
        handle(exception)
    }

    /**
     * Handles the error.
     * @param throwable the error to be handled.
     */
    fun handle(throwable: Throwable) {
        Timber.e(throwable)
        display(throwable)
    }

    /**
     * Displays the given [throwable].
     */
    protected abstract fun display(throwable: Throwable)

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler
}
