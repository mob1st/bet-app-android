package br.com.mob1st.core.design.templates

import androidx.compose.ui.unit.Dp

/**
 * Canonical layout is a layout that can be used for all screen sizes.
 * @see [https://m3.material.io/foundations/layout/canonical-layouts/feed]
 */
data class FeedLayout(
    override val layoutSpec: LayoutSpec,
    private val width: Dp,
) : CanonicalLayout() {
    fun pane(): Pane =
        Pane.single(
            layoutSpec = layoutSpec,
            width = width,
        )
}
