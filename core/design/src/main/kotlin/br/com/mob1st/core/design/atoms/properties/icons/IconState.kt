package br.com.mob1st.core.design.atoms.properties.icons

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@JvmInline
@Immutable
value class IconState private constructor(
    @DrawableRes val id: Int,
) {
    companion object {
        val Add = IconState(id = 0)
    }
}
