package br.com.mob1st.core.design.atoms.colors

/**
 * A tons of colors organized in a Material Design 3 palette.
 * @see [https://m3.material.io/styles/color/overview]
 */
internal data class AppColorPalette constructor(
    val primary: TonalPalette,
    val secondary: TonalPalette,
    val tertiary: TonalPalette,
    val neutral: TonalPalette,
    val neutralVariant: TonalPalette,
    val error: TonalPalette,
) {
    companion object {
        fun init() = AppColorPalette(
            primary = TonalPalette.TropicalIndigo(),
            secondary = TonalPalette.Periwinkle(),
            tertiary = TonalPalette.AntiqueWhite(),
            neutral = TonalPalette.GhostWhite(),
            neutralVariant = TonalPalette.Gray(),
            error = TonalPalette.SalmonPink()
        )
    }
}
