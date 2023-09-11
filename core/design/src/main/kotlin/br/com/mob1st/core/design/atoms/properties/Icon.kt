package br.com.mob1st.core.design.atoms.properties

import androidx.annotation.DrawableRes
import androidx.core.R

@JvmInline
value class Icon private constructor(@DrawableRes val id: Int) {
    companion object {
        fun Next() = Icon(R.drawable.ic_call_answer_low)
    }
}
