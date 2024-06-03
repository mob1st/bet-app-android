package br.com.mob1st.core.design.organisms

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.design.organisms.header.HeaderState

/**
 * Represents the state of a modal.
 */
@Immutable
interface ModalState {
    /**
     * The title of the modal
     */
    val headerState: HeaderState

    /**
     * The message of the modal
     */
    val message: TextState?

    /**
     * If the modal can be dismissed by the user
     */
    val isDismissible: Boolean
}

/**
 * Creates a [ModalState] instance using the default implementation.
 */
fun ModalState(
    headerState: HeaderState,
    message: TextState? = null,
    isDismissible: Boolean = true,
): ModalState =
    ModalStateImpl(
        headerState = headerState,
        message = message,
        isDismissible = isDismissible,
    )

private data class ModalStateImpl(
    override val headerState: HeaderState,
    override val message: TextState?,
    override val isDismissible: Boolean,
) : ModalState
