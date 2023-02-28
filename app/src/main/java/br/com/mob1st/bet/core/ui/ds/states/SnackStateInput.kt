package br.com.mob1st.bet.core.ui.ds.states

import br.com.mob1st.bet.core.ui.ds.molecule.SnackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

interface SnackStateInput {

    fun dismiss(snackState: SnackState<*>)

    fun actionPerformed(snackState: SnackState<*>)
}

class DelegateSnackStateInput : SnackStateInput {

    lateinit var scope: CoroutineScope

    private val dismissInput = MutableSharedFlow<SnackState<*>>()
    val actionPerformedInput = MutableSharedFlow<SnackState<*>>()

    val poll = merge(dismissInput, actionPerformedInput)

    override fun dismiss(snackState: SnackState<*>) {
        scope.launch {
            dismissInput.emit(snackState)
        }
    }

    override fun actionPerformed(snackState: SnackState<*>) {
        scope.launch {
            actionPerformedInput.emit(snackState)
        }
    }
}

inline fun <reified T> Flow<SnackState<*>>.ofType(): Flow<SnackState<T>> = mapNotNull {
    @Suppress("UNCHECKED_CAST")
    it as? SnackState<T>
}
