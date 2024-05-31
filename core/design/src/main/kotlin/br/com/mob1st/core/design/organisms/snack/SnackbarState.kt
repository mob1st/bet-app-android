package br.com.mob1st.core.design.organisms.snack

import br.com.mob1st.core.design.atoms.properties.texts.Text

interface SnackbarState {
    val supporting: Text
    val action: Text?
}

fun SnackbarState(
    supporting: Text,
    action: Text? = null,
): SnackbarState =
    SnackbarStateImpl(
        supporting = supporting,
        action = action,
    )

private data class SnackbarStateImpl(
    override val supporting: Text,
    override val action: Text? = null,
) : SnackbarState
