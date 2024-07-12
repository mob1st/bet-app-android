package br.com.mob1st.core.design.atoms.colors.material.families

/**
 * Encapsulates the color families that can be affected by the ui contrast settings.
 * It's expected to have 6 instances of this class:
 * one for each ui contrast setting (standard, medium and high) * each color theme (light and dark).
 */
data class ContrastedColorFamilies(
    val primary: ColorFamily,
    val secondary: ColorFamily,
    val tertiary: ColorFamily,
    val error: ColorFamily,
    val extensions: TwoCentsColorExtension,
)

/**
 * The brand related colors that doesn't fit in the material color scheme.
 */
data class TwoCentsColorExtension(
    val incomes: ColorFamily,
    val fixedExpenses: ColorFamily,
    val variableExpenses: ColorFamily,
    val seasonalExpenses: ColorFamily,
)
