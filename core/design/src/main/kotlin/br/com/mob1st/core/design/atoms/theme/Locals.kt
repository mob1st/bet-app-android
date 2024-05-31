package br.com.mob1st.core.design.atoms.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.window.layout.DisplayFeature
import br.com.mob1st.core.design.templates.LayoutSpec
import br.com.mob1st.core.design.templates.Pane

/**
 * CompositionLocal for [DisplayFeature]s.
 */
val LocalDisplayFeatures =
    compositionLocalOf {
        emptyList<DisplayFeature>()
    }

val LocalLayoutSpec =
    compositionLocalOf<LayoutSpec> {
        error("CompositionLocal for LocalLayoutSpec not present")
    }

val LocalPane =
    compositionLocalOf<Pane> {
        error("CompositionLocal for LocalPane not present")
    }
