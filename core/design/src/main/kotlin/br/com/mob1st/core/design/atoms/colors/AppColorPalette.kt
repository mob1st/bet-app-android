package br.com.mob1st.core.design.atoms.colors

import androidx.compose.runtime.Immutable

/**
 * A tons of colors organized in a Material Design 3 palette.
 * Avoid uses the constructor directly. Use the [AppColorPalette.Default] factory method instead.
 * @see [https://m3.material.io/styles/color/overview]
 */
@Immutable
internal data class AppColorPalette constructor(
    val primary: TonalPalette,
    val secondary: TonalPalette,
    val tertiary: TonalPalette,
    val neutral: TonalPalette,
    val neutralVariant: TonalPalette,
    val error: TonalPalette,
) {
    companion object {
        /**
         * Factory method for the default [AppColorPalette].
         * It uses function instead of instantiation to avoid creating too much instances during app startup.
         */
        fun Default() = AppColorPalette(
            primary = TonalPalette.TropicalIndigo(),
            secondary = TonalPalette.Periwinkle(),
            tertiary = TonalPalette.AntiqueWhite(),
            neutral = TonalPalette.GhostWhite(),
            neutralVariant = TonalPalette.Gray(),
            error = TonalPalette.SalmonPink()
        )
    }
}
