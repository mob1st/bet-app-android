package br.com.mob1st.features.utils.uimessages

import br.com.mob1st.core.design.organisms.ModalState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Manages the modals to be shown in the UI and allow the consumption of them.
 */
interface ModalStateManager {
    /**
     * The output of the modals to be shown in the UI.
     */
    val modalOutput: StateFlow<ModalState?>

    /**
     * Consumes the modals, removing them from the UI.
     */
    fun consumeModal()
}

/**
 * The delegation to be used by ViewModels to show modals in the UI.
 */
interface ModalDelegate : ModalStateManager {
    /**
     * Shows a modal in the UI.
     */
    fun showModal(modalState: ModalState)
}

/**
 * Creates a [ModalDelegate] instance using the default implementation.
 * @param modalOutput the output of the modals to be shown in the UI.
 */
fun ModalDelegate(modalOutput: MutableStateFlow<ModalState?> = MutableStateFlow(null)): ModalDelegate =
    ModalDelegateImpl(
        _modalOutput = modalOutput,
    )

private class ModalDelegateImpl(
    private val _modalOutput: MutableStateFlow<ModalState?>,
) : ModalDelegate {
    override val modalOutput: StateFlow<ModalState?> = _modalOutput

    override fun showModal(modalState: ModalState) {
        _modalOutput.value = modalState
    }

    override fun consumeModal() {
        _modalOutput.value = null
    }
}
