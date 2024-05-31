package br.com.mob1st.core.design.templates

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPaneStrategy
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy

/**
 * A [CanonicalLayout] that organizes it's panes in a supporting layout.
 * @see [https://m3.material.io/foundations/layout/canonical-layouts/supporting-pane]
 */
data class SupportingLayout(
    override val layoutSpec: LayoutSpec,
) : CanonicalLayout(), CanonicalLayout.Multipane {
    /**
     * The primary pane of this layout, used as the focus and most important area.
     */
    fun primary(width: Dp): Pane =
        Pane(
            layoutSpec = layoutSpec,
            maxWidth = width,
            columnsLimit =
                if (layoutSpec != LayoutSpec.Expanded) {
                    layoutSpec.columnsLimit
                } else {
                    LayoutSpec.Medium.columnsLimit
                },
        )

    /**
     * The secondary pane of this layout, used as a supporting area.
     */
    fun secondary(width: Dp): Pane =
        Pane(
            layoutSpec = layoutSpec,
            maxWidth = width,
            columnsLimit =
                if (layoutSpec == LayoutSpec.Medium) {
                    layoutSpec.columnsLimit
                } else {
                    LayoutSpec.Compact.columnsLimit
                },
        )

    override fun strategy(): TwoPaneStrategy {
        return when (layoutSpec) {
            LayoutSpec.Compact -> error("Unsupported layout spec: $layoutSpec.")
            LayoutSpec.Medium ->
                HorizontalTwoPaneStrategy(
                    splitOffset = 360.dp,
                    offsetFromStart = false,
                    gapWidth = layoutSpec.horizontalSpacing,
                )
            LayoutSpec.Expanded ->
                VerticalTwoPaneStrategy(
                    splitOffset = 360.dp,
                    gapHeight = layoutSpec.horizontalSpacing,
                )
        }
    }
}
