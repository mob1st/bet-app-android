package br.com.mob1st.core.design.templates

import androidx.compose.runtime.Immutable
import com.google.accompanist.adaptive.TwoPaneStrategy

/**
 * Canonical layout is a layout that can be used for all screen sizes.
 * @see [https://m3.material.io/foundations/layout/canonical-layouts/overview]
 */
@Immutable
sealed class CanonicalLayout {
    /**
     * The specification used by this layout to design it's panes
     */
    abstract val layoutSpec: LayoutSpec

    /**
     * Canonical layouts that can support multiple panes at the same time have to implement this interface
     */
    fun interface Multipane {
        /**
         * The strategy used to split the screen into multiple panes
         */
        fun strategy(): TwoPaneStrategy
    }
}
