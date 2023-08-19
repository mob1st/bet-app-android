package br.com.mob1st.core.design.templates

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.Dp

/**
 * A factory for creating [Pane]s for a TwoPane layout.
 */
internal fun interface TwoPaneFactory {
    fun create(useSingleWhenMediumWindow: Boolean, windowWidthSizeClass: WindowWidthSizeClass, width: Dp): Pane
}
