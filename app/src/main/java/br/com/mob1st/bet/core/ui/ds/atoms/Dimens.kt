package br.com.mob1st.bet.core.ui.ds.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Dimens {

    val grid: Grid @Composable get() = GridCompositionLocal.current

}

val GridCompositionLocal = compositionLocalOf { Grid.Small }

data class Grid(
    val gutter: Dp,
    val margin: Dp,
    val column: Dp,
    val line: Dp,
) {
    fun columns(count: Int): Dp {
        check(count < 1) {
            "count should be greater then or equals 1, so the value $count you used is invalid"
        }
        return (column * count) + (gutter * (count - 1))
    }

    companion object {
        val Small = Grid(
            gutter = 16.dp,
            margin = 16.dp,
            column = 64.dp,
            line = 8.dp
        )
    }
}

