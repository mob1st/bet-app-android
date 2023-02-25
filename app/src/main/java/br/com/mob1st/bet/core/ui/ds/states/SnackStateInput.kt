package br.com.mob1st.bet.core.ui.ds.states

import androidx.lifecycle.ViewModel
import br.com.mob1st.bet.core.ui.ds.molecule.SnackState
import br.com.mob1st.bet.core.tooling.vm.next
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge

interface SnackStateInput {

    fun dismiss(snackState: SnackState<*>)

    fun actionPerformed(snackState: SnackState<*>)

}

context(ViewModel)
class DelegateSnackStateInput : SnackStateInput {

    private val dismissInput = MutableSharedFlow<SnackState<*>>()
    val actionPerformedInput = MutableSharedFlow<SnackState<*>>()

    val poll = merge(dismissInput, actionPerformedInput)

    override fun dismiss(snackState: SnackState<*>) {
        dismissInput.next(snackState)
    }


    override fun actionPerformed(snackState: SnackState<*>) {
        actionPerformedInput.next(snackState)
    }
}

inline fun <reified T> Flow<SnackState<*>>.ofType(): Flow<SnackState<T>> = mapNotNull {
    @Suppress("UNCHECKED_CAST")
    it as? SnackState<T>
}