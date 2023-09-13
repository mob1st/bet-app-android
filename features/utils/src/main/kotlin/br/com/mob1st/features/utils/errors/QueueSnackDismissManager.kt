package br.com.mob1st.features.utils.errors

import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.core.state.contracts.SnackbarDismissManager
import br.com.mob1st.core.state.managers.ViewModelDelegate
import br.com.mob1st.features.utils.navigation.SettingsNavigationEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.LinkedList
import java.util.Queue

/**
 * Provides state management for [SnackState]s using a Queue strategy to add/remove snacks.
 * It's useful for ViewModels to implement common snack state management and some default actions that should be
 * like triggering navigation into the device's network settings when a [CommonError.NoConnectivity] is sent.
 */
interface QueueSnackDismissManager : ViewModelDelegate<SnackState?>, SnackbarDismissManager {

    /**
     * Dismisses the current snack
     */
    fun offer(snack: SnackState, performAction: (suspend () -> Unit)? = null)

    /**
     * Dismisses the current snack using the default snack action
     */
    fun offerCommonErrorSnack(value: Throwable)

    /**
     * perform the first snack action in the queue provided in [offer]
     */
    suspend fun performSnackAction()
}

/**
 * Creates a [QueueSnackDismissManager] instance using the default implementation.
 */
fun QueueSnackManager(settingsEventBus: SettingsNavigationEventBus): QueueSnackDismissManager =
    QueueSnackManagerImpl(settingsEventBus)

private class QueueSnackManagerImpl(
    private val settingsEventBus: SettingsNavigationEventBus,
) : QueueSnackDismissManager {

    private val _output: MutableStateFlow<Queue<SnackSource>> = MutableStateFlow(LinkedList())

    override fun output(scope: CoroutineScope): StateFlow<SnackState?> {
        return _output
            .map { it.firstOrNull()?.first }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null
            )
    }

    override fun dismissSnack() {
        _output.update {
            it.poll()
            it
        }
    }

    override suspend fun performSnackAction() {
        _output.value.firstOrNull()?.second?.invoke()
    }

    /**
     * Common error handling that shows the [CommonError] snackbar.
     * Use this method for common error handling in ViewModels.
     * @see CommonError.from
     */
    override fun offerCommonErrorSnack(value: Throwable) {
        val commonError = CommonError.from(value)
        offer(commonError.snack) {
            defaultPerform(commonError)
        }
    }

    /**
     * Offers the given [snack] to the queue.
     *
     * Only new snackbars are displayed, so if a
     * @param snack is the new snack state
     * @param performAction is the action to be performed when the user clicks on the snack action. Try to implement
     * this interface using kotlin object, ViewModel scoped singletons or function references (::) to avoid issues with
     * @see SnackPerformAction
     */
    override fun offer(snack: SnackState, performAction: SnackPerformAction?) {
        _output.update { list ->
            if (list.all { it.first != snack }) {
                val action: SnackPerformAction = suspend {
                    pollAndInvoke(snack, performAction)
                }
                list.offer(snack to action)
            }
            list
        }
    }

    private suspend fun pollAndInvoke(snack: SnackState, performAction: SnackPerformAction?) {
        _output.update { list ->
            list -= list.first { it.first == snack }
            list
        }
        performAction?.invoke()
    }

    private suspend fun defaultPerform(commonError: CommonError) {
        if (commonError is CommonError.NoConnectivity) {
            settingsEventBus.postEvent(SettingsNavigationEventBus.Target.Connection)
        }
    }
}

private typealias SnackSource = Pair<SnackState, suspend () -> Unit?>
private typealias SnackPerformAction = suspend () -> Unit
