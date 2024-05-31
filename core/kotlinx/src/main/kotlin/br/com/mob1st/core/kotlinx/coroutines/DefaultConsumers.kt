package br.com.mob1st.core.kotlinx.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

/**
 * Transforms a [Flow] of [T] into a [Flow] of [Result] of [T].
 * It's useful to be exposed for operations that cannot crash but at the same time should expose the failure.
 *
 * @param T the type of the [Flow]
 */
fun <T, R> Flow<T>.mapCatching(
    map: (T) -> R,
    catch: (Throwable) -> Unit,
): Flow<R> =
    map {
        Result.success(it)
    }.catch { throwable ->
        emit(Result.failure(throwable))
    }.transform { result ->
        result
            .onSuccess { value ->
                emit(map(value))
            }
            .onFailure { throwable ->
                catch(throwable)
            }
    }
