package br.com.mob1st.core.design.atoms.theme

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.compositionLocalOf
import androidx.window.layout.DisplayFeature

/**
 * CompositionLocal for [DisplayFeature]s.
 */
val LocalDisplayFeatures = compositionLocalOf {
    emptyList<DisplayFeature>()
}

/**
 * CompositionLocal for [WindowWidthSizeClass].
 */
val LocalWindowWidthSizeClass = compositionLocalOf<WindowWidthSizeClass> {
    error("CompositionLocal for LocalWindowSizeClass not present")
}
