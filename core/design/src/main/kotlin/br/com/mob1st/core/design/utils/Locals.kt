package br.com.mob1st.core.design.utils

import androidx.compose.runtime.compositionLocalOf
import java.util.Locale

val LocalLocale = compositionLocalOf<Locale> {
    Locale.getDefault()
}
