package br.com.mob1st.bet.core.ui.ds.atoms

import androidx.annotation.DrawableRes
import br.com.mob1st.bet.core.tooling.androidx.TextData

data class IconState(
    @DrawableRes val resId: Int,
    val contentDescription: TextData?,
)
