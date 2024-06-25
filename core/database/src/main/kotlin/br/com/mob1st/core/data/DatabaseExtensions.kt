package br.com.mob1st.core.data

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun Transacter.suspendTransaction(
    context: CoroutineContext,
    noEnclosing: Boolean = false,
    body: TransactionWithoutReturn.() -> Unit,
) = withContext(context) {
    transaction(noEnclosing, body)
}
