package br.com.mob1st.core.design.templates

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.Dp
import br.com.mob1st.core.design.atoms.spacing.Spacings

/**
 * A layout specification used to align and distribute the panes in the UI.
 */
@Suppress("MagicNumber")
enum class LayoutSpec(
    /**
     * The horizontal spacing that can be applied in the panes.
     */
    val horizontalSpacing: Dp,
    /**
     * The maximum number of columns that can be used in the panes.
     */
    val columnsLimit: ColumnsLimit,
) {
    Compact(
        horizontalSpacing = Spacings.x4,
        columnsLimit = ColumnsLimit.Four,
    ),

    Medium(
        horizontalSpacing = Spacings.x6,
        columnsLimit = ColumnsLimit.Eight,
    ),

    Expanded(
        horizontalSpacing = Spacings.x6,
        columnsLimit = ColumnsLimit.Twelve,
    ),
    ;

    companion object {
        fun of(windowWidthSizeClass: WindowWidthSizeClass) =
            when (windowWidthSizeClass) {
                WindowWidthSizeClass.Compact -> Compact
                WindowWidthSizeClass.Medium -> Medium
                WindowWidthSizeClass.Expanded -> Expanded
                else -> error("Unsupported window width size class: $windowWidthSizeClass.")
            }
    }
}

@JvmInline
value class ColumnsLimit private constructor(val value: Int) {
    companion object {
        val Four = ColumnsLimit(4)
        val Eight = ColumnsLimit(8)
        val Twelve = ColumnsLimit(12)
    }
}
