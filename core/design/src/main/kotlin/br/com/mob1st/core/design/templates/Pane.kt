package br.com.mob1st.core.design.templates

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.properties.dimensions.Dimension

/**
 * A pane that can be used to display content in a specif part of the layout.
 */
@Immutable
data class Pane(
    val layoutSpec: LayoutSpec,
    val columnsLimit: ColumnsLimit,
    val maxWidth: Dp,
) {

    val horizontalMargins = if (layoutSpec == LayoutSpec.Compact) {
        LayoutSpec.Compact.horizontalSpacing
    } else {
        0.dp
    }

    private val gutter = layoutSpec.horizontalSpacing
    private val emptyWidth = (horizontalMargins * 2) + gutter * (columnsLimit.value - 1)
    private val columnWidth: Dp = (maxWidth - emptyWidth) / columnsLimit.value

    /**
     * Calculates the width of a column based on the number of columns in this pane.
     * @throws [IllegalArgumentException] if the number of columns is not in the range
     */
    fun columns(count: Int): Dimension = when {
        count in 1.rangeUntil(columnsLimit.value) -> {
            val dp = (columnWidth * count) + (gutter * (count - 1))
            Dimension.Fixed(dp)
        }
        count >= columnsLimit.value -> Dimension.FillMax()
        else -> throw IllegalArgumentException(
            "Invalid column count: $count. Current range is 1..${columnsLimit.value}."
        )
    }

    companion object {
        fun single(layoutSpec: LayoutSpec, width: Dp): Pane = Pane(
            layoutSpec = layoutSpec,
            maxWidth = width,
            columnsLimit = layoutSpec.columnsLimit
        )
    }
}
