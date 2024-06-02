package br.com.mob1st.core.design.molecules.buttons

import br.com.mob1st.core.design.atoms.properties.texts.TextState

interface ButtonState {
    val text: TextState
    val isLoading: Boolean
    val isEnabled: Boolean
}

fun ButtonState(
    text: TextState,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
): ButtonState =
    ButtonStateImpl(
        text = text,
        isLoading = isLoading,
        isEnabled = isEnabled,
    )

private data class ButtonStateImpl(
    override val text: TextState,
    override val isLoading: Boolean = false,
    override val isEnabled: Boolean = true,
) : ButtonState
