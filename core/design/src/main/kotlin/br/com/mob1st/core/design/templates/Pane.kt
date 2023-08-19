package br.com.mob1st.core.design.templates

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A pane that can be used to display content in a specif part of the layout.
 */
@Immutable
data class Pane(

    /**
     * Whether this pane is the first of the two panes.
     */
    val isFirst: Boolean,

    /**
     * Whether this pane is the only one being shown.
     */
    val isSingle: Boolean,

    /**
     * The number of columns to use in this pane.
     */
    val columns: Int,

    /**
     * The horizontal padding to apply to the pane and the gutter between columns.
     */
    val spacing: Dp,

    /**
     * The maximum width of the pane.
     */
    val maxWidth: Dp,
) {

    val columnsWidth: Dp get() {
        return 0.dp
    }

    /**
     * Calculates the width of the columns based on the given [count], the gutters and the horizontal paddings.
     */
    fun columns(count: Int): Dp {
        return 0.dp
    }
}
