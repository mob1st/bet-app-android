package br.com.mob1st.bet.core.ui.ds.organisms

import br.com.mob1st.bet.core.ui.ds.molecule.ButtonState

data class ButtonsBarState(
    val primary: ButtonState.Primary,
    val secondary: ButtonState?
)
