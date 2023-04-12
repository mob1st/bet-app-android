package br.com.mob1st.core.state.async

/**
 * Represents the state of an asynchronous operation.
 */
internal sealed class AsyncState {

    /**
     * The operation has not been triggered yet.
     */
    object NotLaunchedYet : AsyncState()

    /**
     * The operation is loading.
     */
    object Loading : AsyncState()

    /**
     * The operation has failed.
     * @param cause the cause of the failure
     */
    class Failure(val cause: Throwable) : AsyncState()

    /**
     * The operation has succeeded.
     * @param output the output of the operation
     */
    class Success<O>(val output: O) : AsyncState()

    /**
     * Returns true if the operation is [Loading].
     */
    fun isLoading(): Boolean = this is Loading

    /**
     * Returns the output of the operation if it is [Success].
     * Otherwise, returns null.
     */
    fun <T> data(): T? = if (this is Success<*>) {
        @Suppress("UNCHECKED_CAST")
        output as? T
    } else {
        null
    }

    /**
     * Returns the cause of the failure if the operation is [Failure].
     * Otherwise, returns null.
     */
    fun failure(): Throwable? = if (this is Failure) {
        cause
    } else {
        null
    }
}
