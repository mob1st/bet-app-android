package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.features.utils.errors.QueueSnackDismissManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A fake implementation of [QueueSnackDismissManager] that allows to test the state of the manager.
 *
 * It has no implementation, just state holding. This means that it will not remove snackbars if [dismissCalls] or
 * [performActionCalls] are called and it will not perform any offered action if [performActionCalls] are called
 * Invoke the actions given to [offer] manually if you want to test its invocation.
 *
 * @param output The [MutableStateFlow] that will be used as the [QueueSnackDismissManager.output].
 */
class FakeQueueSnackDismissManager(
    private val output: MutableStateFlow<SnackState?> = MutableStateFlow(null),
) : QueueSnackDismissManager {

    private val _offers = mutableListOf<Pair<SnackState, suspend () -> Unit>>()

    /**
     * The list of [SnackState]s offered to the manager.
     */
    val offers: List<Pair<SnackState, suspend () -> Unit>> = _offers

    private val _commonErrorOffers = mutableListOf<Throwable>()

    /**
     * The list of [Throwable]s offered to the manager.
     */
    val commonErrorOffers: List<Throwable> = _commonErrorOffers

    private val _dismissCalls = mutableListOf<Unit>()

    /**
     * The number of times [dismissSnack] was called.
     */
    val dismissCalls: Int get() = _dismissCalls.size

    private val _performActionCalls = mutableListOf<Unit>()

    /**
     * The number of times [performSnackAction] was called.
     */
    val performActionCalls: Int = _performActionCalls.size

    override fun offer(snack: SnackState, performAction: (suspend () -> Unit)?) {
        _offers += snack to (performAction ?: {})
        output.value = snack
    }

    override fun offerCommonErrorSnack(value: Throwable) {
        _commonErrorOffers += value
    }

    override fun dismissSnack() {
        _dismissCalls += Unit
    }

    override suspend fun performSnackAction() {
        _performActionCalls += Unit
    }

    override fun output(scope: CoroutineScope): StateFlow<SnackState?> {
        return output.asStateFlow()
    }
}

/**
 * True if [FakeQueueSnackDismissManager.offer] was called, false otherwise.
 */
fun FakeQueueSnackDismissManager.isOffered(snack: SnackState) = offers.any { it.first == snack }

/**
 * True if [FakeQueueSnackDismissManager.offerCommonErrorSnack] was called, false otherwise.
 */
fun FakeQueueSnackDismissManager.isOffered() = offers.isNotEmpty()

/**
 * True if [FakeQueueSnackDismissManager.offerCommonErrorSnack] was called with anything of the given type [T], false
 * otherwise.
 * @param T The type of the [Throwable] to check.
 */
inline fun <reified T : Throwable> FakeQueueSnackDismissManager.isCommonErrorOfferedFor() = commonErrorOffers.any {
    it is T
}

/**
 * True if [FakeQueueSnackDismissManager.offerCommonErrorSnack] was called, false otherwise.
 */
fun FakeQueueSnackDismissManager.isCommonErrorOffered() = commonErrorOffers.isNotEmpty()

/**
 * True if [FakeQueueSnackDismissManager.dismissSnack] was called, false otherwise.
 */
fun FakeQueueSnackDismissManager.isDismissed() = dismissCalls > 0

/**
 * True if [FakeQueueSnackDismissManager.performSnackAction] was called, false otherwise.
 */
fun FakeQueueSnackDismissManager.isPerformActionCalled() = performActionCalls > 0
