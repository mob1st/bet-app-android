package br.com.mob1st.core.design.organisms.snack

import br.com.mob1st.core.design.atoms.properties.texts.TextState

interface SnackbarState {
    val supporting: TextState
    val action: TextState?
}

fun SnackbarState(
    supporting: TextState,
    action: TextState? = null,
): SnackbarState =
    SnackbarStateImpl(
        supporting = supporting,
        action = action,
    )

private data class SnackbarStateImpl(
    override val supporting: TextState,
    override val action: TextState? = null,
) : SnackbarState
