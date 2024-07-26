package br.com.mob1st.core.data

import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithoutReturn
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Converts the receiver [Query] to a flow of lists of the query results.
 * It map each generated SQLDelight DTO of type [T] to a list of type [R].
 * @param T The type of the SQLDelight DTO.
 * @param R The type of the result object.
 * @param context The coroutine context to run the flow.
 * @param transform The transformation function to convert the DTO to the domain object.
 * @return The flow of lists of domain objects.
 */
fun <T : Any, R> Query<T>.asFlowListEach(
    context: CoroutineContext,
    transform: (T) -> R,
) = asFlowList(context) { list ->
    list.map(transform)
}

/**
 * Converts the receiver [Query] to a flow of lists of the query results.
 * It maps the generated SQLDelight DTO of type [T] to a object of type [R].
 * @param T The type of the SQLDelight DTO.
 * @param R The type of the domain object.
 * @param context The coroutine context to run the flow.
 * @param transform The transformation function to convert the DTO to the domain object.
 */
fun <T : Any, R> Query<T>.asFlowList(
    context: CoroutineContext,
    transform: (List<T>) -> List<R>,
) = asFlow().mapToList(context).map { list ->
    transform(list)
}

/**
 * Converts the receiver [Query] to a flow of single results of the query.
 * It maps the generated SQLDelight DTO of type [T] to a object of type [R].
 * @param T The type of the SQLDelight DTO.
 * @param R The type of the result object.
 * @param context The coroutine context to run the flow.
 * @param transform The transformation function to convert the DTO to the domain object.
 * @return The flow of domain objects.
 */
fun <T : Any, R> Query<T>.asFlowSingle(
    context: CoroutineContext,
    transform: (T) -> R,
) = asFlow().mapToOne(context).map { t ->
    transform(t)
}

/**
 * Handy extension to run a transaction in a suspend context.
 * @param context The coroutine context to run the transaction.
 * @param noEnclosing Whether the transaction should be run without an enclosing transaction.
 * @param body The transaction body. It has as receiver the [TransactionWithoutReturn] object, which allows the usage of
 * its methods to interact with the database during the transaction.
 */
suspend fun Transacter.suspendTransaction(
    context: CoroutineContext,
    noEnclosing: Boolean = false,
    body: TransactionWithoutReturn.() -> Unit,
) = withContext(context) {
    transaction(noEnclosing, body)
}
