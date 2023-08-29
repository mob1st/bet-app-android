package br.com.mob1st.core.design.atoms.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.window.layout.DisplayFeature
import br.com.mob1st.core.design.templates.LayoutSpec

/**
 * CompositionLocal for [DisplayFeature]s.
 */
val LocalDisplayFeatures = compositionLocalOf {
    emptyList<DisplayFeature>()
}

val LocalLayoutSpec = compositionLocalOf<LayoutSpec> {
    error("CompositionLocal for LocalLayoutSpec not present")
}
