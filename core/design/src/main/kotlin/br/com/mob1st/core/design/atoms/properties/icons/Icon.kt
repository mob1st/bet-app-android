package br.com.mob1st.core.design.atoms.properties.icons

import androidx.annotation.DrawableRes

@JvmInline
value class Icon private constructor(
    @DrawableRes val id: Int,
)
