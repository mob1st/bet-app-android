package br.com.mob1st.bet.core.ui.ds.molecule

import br.com.mob1st.bet.core.tooling.androidx.TextData

data class ButtonState(
    val text: TextData,
    val loading: Boolean = false,
)