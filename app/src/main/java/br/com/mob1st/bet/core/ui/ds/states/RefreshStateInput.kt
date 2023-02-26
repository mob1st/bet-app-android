package br.com.mob1st.bet.core.ui.ds.states

import androidx.lifecycle.ViewModel
import br.com.mob1st.bet.core.tooling.vm.next
import kotlinx.coroutines.flow.MutableSharedFlow

interface RefreshStateInput {

    fun refresh()
}

context(ViewModel)
class DelegateRefreshStateManager : RefreshStateInput {

    val refreshInput = MutableSharedFlow<Unit>()

    override fun refresh() {
        refreshInput.next(Unit)
    }
}
