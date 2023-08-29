package br.com.mob1st.core.design.atoms.spacing

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The default spacings used in the app.
 * Always use this values to define hardcoded spacings, never use standalone dp values.
 * This is a list of multiples by 4dp, starting from x1 (4dp) until x16(64dp).
 * If more than x16 is needed, use dynamic values such as screen % or available space for a component.
 */
internal object Spacings {

    val x1: Dp = 4.dp
    val x2: Dp = 8.dp
    val x3: Dp = 12.dp
    val x4: Dp = 16.dp
    val x5: Dp = 20.dp
    val x6: Dp = 24.dp
    val x7: Dp = 28.dp
    val x8: Dp = 32.dp
    val x9: Dp = 36.dp
    val x10: Dp = 40.dp
    val x11: Dp = 44.dp
    val x12: Dp = 48.dp
    val x13: Dp = 52.dp
    val x14: Dp = 56.dp
    val x15: Dp = 60.dp
    val x16: Dp = 64.dp
}
