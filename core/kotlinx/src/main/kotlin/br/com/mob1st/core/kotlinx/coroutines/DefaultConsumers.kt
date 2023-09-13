package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Transforms a [Flow] of [T] into a [Flow] of [Result] of [T].
 * It's useful to be exposed for operations that cannot crash but at the same time should expose the failure.
 *
 * @param T the type of the [Flow]
 */
fun <T> Flow<T>.asResultFlow(): Flow<Result<T>> = map {
    Result.success(it)
}.catch {
    emit(Result.failure(it))
}
