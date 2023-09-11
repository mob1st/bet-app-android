package br.com.mob1st.features.utils.errors

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.core.kotlinx.collections.QueueStateFlow
import br.com.mob1st.core.kotlinx.collections.updateQueue
import br.com.mob1st.core.state.contracts.SnackbarManager
import br.com.mob1st.core.state.managers.StateManager
import br.com.mob1st.features.utils.navigation.SettingsNavigationEventBus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DefaultSnackManager : StateManager(), SnackbarManager {

    private val state: QueueStateFlow<SnackSource> = QueueStateFlow()

    val peeks: Flow<SnackState?> = state.map { it.peek()?.first }
    override fun dismissSnack() {
        state.updateQueue { poll() }
    }

    override fun performSnackAction() {
        state.updateQueue {
            poll()?.second?.invoke()
        }
    }

    private fun defaultPerform() {
        managerScope.launch {
            SettingsNavigationEventBus.postEvent(SettingsNavigationEventBus.Target.Connection)
        }
    }

    fun offerCommonErrorSnack(value: Throwable) = offer(
        CommonError.from(value).snack,
        ::defaultPerform
    )

    fun offer(value: SnackState, performAction: SnackPerformAction? = null) = state.updateQueue {
        if (all { it.first != value }) {
            offer(value to performAction)
        }
    }
}

typealias SnackSource = Pair<SnackState, SnackPerformAction?>

@Immutable
fun interface SnackPerformAction {
    operator fun invoke()
}
