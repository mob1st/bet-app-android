package br.com.mob1st.features.utils.errors

import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.core.kotlinx.collections.QueueStateFlow
import br.com.mob1st.core.kotlinx.collections.updateQueue
import br.com.mob1st.core.state.contracts.SnackbarManager
import br.com.mob1st.core.state.managers.StateManager
import br.com.mob1st.features.utils.navigation.SettingsNavigationEventBus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Provides state management for [SnackState]s using a Queue strategy to add/remove snacks.
 * It's useful for ViewModels to implement common snack state management and some default actions that should be
 * like triggering navigation into the device's network settings when a [CommonError.NoConnectivity] is sent.
 * As a state manager, <b>always remember to add it in the ViewModel's closeable bags</b>.
 */
class QueueSnackManager : StateManager(), SnackbarManager {

    private val state: QueueStateFlow<SnackSource> = QueueStateFlow()

    /**
     * The current snack state
     */
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

    /**
     * Common error handling that shows the [CommonError] snackbar.
     * Use this method for common error handling in ViewModels.
     * @see CommonError.from
     */
    fun offerCommonErrorSnack(value: Throwable) = offer(
        CommonError.from(value).snack,
        ::defaultPerform
    )

    /**
     * Offers the given [snack] to the queue.
     *
     * Only new snackbars are displayed, so if a
     * @param snack is the new snack state
     * @param performAction is the action to be performed when the user clicks on the snack action. Try to implement
     * this interface using kotlin object, ViewModel scoped singletons or function references (::) to avoid issues with
     * @see SnackPerformAction
     */
    fun offer(snack: SnackState, performAction: SnackPerformAction? = null) = state.updateQueue {
        if (all { it.first != snack }) {
            offer(snack to performAction)
        }
    }
}

/**
 * Represents an action to be performed when the user clicks on the snack action.
 * Try to implement this interface using kotlin object, ViweModel scoped singletons or function references (::) to avoid
 * issues with equality.
 */
fun interface SnackPerformAction {

    /**
     * Performs the action
     */
    operator fun invoke()
}

private typealias SnackSource = Pair<SnackState, SnackPerformAction?>
