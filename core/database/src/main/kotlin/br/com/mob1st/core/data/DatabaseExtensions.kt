package br.com.mob1st.core.data

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

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
