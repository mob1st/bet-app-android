package br.com.mob1st.core.design.organisms

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.Text
import br.com.mob1st.core.design.molecules.buttons.ButtonBar
import br.com.mob1st.core.design.molecules.texts.Header

/**
 * Represents the state of a modal.
 */
@Immutable
interface ModalState {
    /**
     * The title of the modal
     */
    val header: Header

    /**
     * The message of the modal
     */
    val message: Text?

    /**
     * The negative action of the modal
     */
    val positiveAction: ButtonBar

    /**
     * If the modal can be dismissed by the user
     */
    val isDismissible: Boolean
}

/**
 * Creates a [ModalState] instance using the default implementation.
 */
fun ModalState(
    header: Header,
    buttonBar: ButtonBar,
    message: Text? = null,
    isDismissible: Boolean = true,
): ModalState =
    ModalStateImpl(
        header = header,
        message = message,
        positiveAction = buttonBar,
        isDismissible = isDismissible,
    )

private data class ModalStateImpl(
    override val header: Header,
    override val message: Text?,
    override val positiveAction: ButtonBar,
    override val isDismissible: Boolean,
) : ModalState
