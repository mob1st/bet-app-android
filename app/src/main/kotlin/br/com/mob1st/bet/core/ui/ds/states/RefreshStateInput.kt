package br.com.mob1st.bet.core.ui.ds.states

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

interface RefreshStateInput {

    fun refresh()
}

class DelegateRefreshStateInput : RefreshStateInput {
    lateinit var scope: CoroutineScope
    val refreshInput = MutableSharedFlow<Unit>()

    override fun refresh() {
        scope.launch {
            refreshInput.emit(Unit)
        }
    }
}
