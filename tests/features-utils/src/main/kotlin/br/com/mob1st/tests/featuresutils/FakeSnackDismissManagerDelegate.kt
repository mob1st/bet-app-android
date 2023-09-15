package br.com.mob1st.tests.featuresutils

import br.com.mob1st.core.design.organisms.snack.SnackState
import br.com.mob1st.features.utils.errors.SnackDismissManagerDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A fake implementation of [SnackDismissManagerDelegate] that allows to test the state of the manager.
 *
 * It has no implementation, just state holding. This means that it will not remove snackbars if [dismissCalls] or
 * [performActionCalls] are called and it will not perform any offered action if [performActionCalls] are called
 * Invoke the actions given to [offer] manually if you want to test its invocation.
 *
 * @param output The [MutableStateFlow] that will be used as the [SnackDismissManagerDelegate.output].
 */
class FakeSnackDismissManagerDelegate(
    private val output: MutableStateFlow<SnackState?> = MutableStateFlow(null),
) : SnackDismissManagerDelegate {

    /**
     * The list of [SnackState]s offered to the manager.
     */
    val offers: List<Pair<SnackState, suspend () -> Unit>> get() = _offers.toList()
    private val _offers = mutableListOf<Pair<SnackState, suspend () -> Unit>>()

    /**
     * The list of [Throwable]s offered to the manager.
     */
    val commonErrorOffers: List<Throwable> get() = _commonErrorOffers.toList()
    private val _commonErrorOffers = mutableListOf<Throwable>()

    /**
     * The number of times [dismissSnack] was called.
     */
    val dismissCalls: Int get() = _dismissCalls.size
    private val _dismissCalls = mutableListOf<Unit>()

    /**
     * The number of times [performSnackAction] was called.
     */
    val performActionCalls: Int get() = _performActionCalls.size
    private val _performActionCalls = mutableListOf<Unit>()

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
 * True if [FakeSnackDismissManagerDelegate.offer] was called, false otherwise.
 */
fun FakeSnackDismissManagerDelegate.isOffered(snack: SnackState) = offers.any { it.first == snack }

/**
 * True if [FakeSnackDismissManagerDelegate.offerCommonErrorSnack] was called, false otherwise.
 */
fun FakeSnackDismissManagerDelegate.isOffered() = offers.isNotEmpty()

/**
 * True if [FakeSnackDismissManagerDelegate.offerCommonErrorSnack] was called with anything of the given type [T], false
 * otherwise.
 * @param T The type of the [Throwable] to check.
 */
inline fun <reified T : Throwable> FakeSnackDismissManagerDelegate.isCommonErrorOfferedFor() = commonErrorOffers.any {
    it is T
}

/**
 * True if [FakeSnackDismissManagerDelegate.offerCommonErrorSnack] was called, false otherwise.
 */
fun FakeSnackDismissManagerDelegate.isCommonErrorOffered() = commonErrorOffers.isNotEmpty()

/**
 * True if [FakeSnackDismissManagerDelegate.dismissSnack] was called, false otherwise.
 */
fun FakeSnackDismissManagerDelegate.isDismissed() = dismissCalls > 0

/**
 * True if [FakeSnackDismissManagerDelegate.performSnackAction] was called, false otherwise.
 */
fun FakeSnackDismissManagerDelegate.isPerformActionCalled() = performActionCalls > 0
