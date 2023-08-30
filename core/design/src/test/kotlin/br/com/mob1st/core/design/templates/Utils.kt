package br.com.mob1st.core.design.templates

import androidx.compose.ui.unit.dp
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map

object ScreenWidth {
    val compact = Arb.int(360, 599).map { it.dp }
    val medium = Arb.int(600, 859).map { it.dp }
    val expanded = Arb.int(860, 1920).map { it.dp }
}
