package br.com.mob1st.bet.core.ui.ds.organisms

import br.com.mob1st.bet.core.tooling.androidx.TextData

data class ExplainerBottomSheetState(
    val title: TextData,
    val description: TextData,
    val buttonsBarState: ButtonsBarState
)