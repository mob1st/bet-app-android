package br.com.mob1st.core.design.molecules.buttons

import br.com.mob1st.core.design.atoms.properties.texts.Text

interface ButtonState {
    val text: Text
    val isLoading: Boolean
    val isEnabled: Boolean
}

fun ButtonState(
    text: Text,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
): ButtonState =
    ButtonStateImpl(
        text = text,
        isLoading = isLoading,
        isEnabled = isEnabled,
    )

private data class ButtonStateImpl(
    override val text: Text,
    override val isLoading: Boolean = false,
    override val isEnabled: Boolean = true,
) : ButtonState
