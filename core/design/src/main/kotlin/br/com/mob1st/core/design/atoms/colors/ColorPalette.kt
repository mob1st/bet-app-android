package br.com.mob1st.core.design.atoms.colors

import androidx.compose.runtime.Immutable

/**
 * A palette of colors organized in a Material Design 3 palette.
 * Avoid uses the constructor directly. Use the [ColorPalette.Default] factory method instead.
 * @see [https://m3.material.io/styles/color/overview]
 */
@Immutable
internal data class ColorPalette constructor(
    val primary: ColorTonal,
    val secondary: ColorTonal,
    val tertiary: ColorTonal,
    val neutral: ColorTonal,
    val neutralVariant: ColorTonal,
    val error: ColorTonal,
) {
    companion object {
        /**
         * Factory method for the default [ColorPalette].
         * It uses function instead of instantiation to avoid creating too much instances during app startup.
         */
        fun Default() =
            ColorPalette(
                primary = ColorTonal.TropicalIndigo(),
                secondary = ColorTonal.Periwinkle(),
                tertiary = ColorTonal.AntiqueWhite(),
                neutral = ColorTonal.GhostWhite(),
                neutralVariant = ColorTonal.Gray(),
                error = ColorTonal.SalmonPink(),
            )
    }
}
