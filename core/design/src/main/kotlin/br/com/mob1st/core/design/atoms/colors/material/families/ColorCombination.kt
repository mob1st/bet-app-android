package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color

/**
 * A combination of colors that can be used to style a component.
 * @property background The background color.
 * @property content The content color.
 */
data class ColorCombination(
    val background: Color,
    val content: Color,
)
