package br.com.mob1st.core.design.templates
import androidx.compose.ui.unit.Dp
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPaneStrategy

/**
 * A [CanonicalLayout] that organizes it's panes in list and a detail.
 * @see [https://m3.material.io/foundations/layout/canonical-layouts/list-detail]
 */
data class ListDetailLayout(
    override val layoutSpec: LayoutSpec,
    /**
     * Whether to use a single pane when the window is medium.
     */
    val useSingleWhenMedium: Boolean,
) : CanonicalLayout(), CanonicalLayout.Multipane {
    /**
     * Creates a [Pane] for the list pane of this layout.
     */
    fun list(width: Dp): Pane {
        val isSingle = layoutSpec == LayoutSpec.Compact || (layoutSpec == LayoutSpec.Medium && useSingleWhenMedium)
        return Pane(
            layoutSpec = layoutSpec,
            maxWidth = width,
            columnsLimit =
                if (isSingle) {
                    layoutSpec.columnsLimit
                } else {
                    ColumnsLimit.Four
                },
        )
    }

    /**
     * Creates a [Pane] for the detail pane of this layout.
     */
    fun detail(width: Dp): Pane {
        val isCompact = layoutSpec == LayoutSpec.Compact
        val isSingleOnMedium = layoutSpec == LayoutSpec.Medium && useSingleWhenMedium
        require(!isCompact && !isSingleOnMedium) {
            "Detail pane is only available for medium and expanded layouts."
        }

        return Pane(
            layoutSpec = layoutSpec,
            maxWidth = width,
            columnsLimit =
                if (layoutSpec == LayoutSpec.Medium) {
                    LayoutSpec.Compact.columnsLimit
                } else {
                    LayoutSpec.Medium.columnsLimit
                },
        )
    }

    override fun strategy(): TwoPaneStrategy {
        require(layoutSpec >= LayoutSpec.Medium) {
            "Window must be medium or expanded. Current is $layoutSpec."
        }
        return HorizontalTwoPaneStrategy(
            splitFraction =
                if (layoutSpec == LayoutSpec.Expanded) {
                    0.35f
                } else {
                    0.50f
                },
            gapWidth = layoutSpec.horizontalSpacing,
        )
    }
}
