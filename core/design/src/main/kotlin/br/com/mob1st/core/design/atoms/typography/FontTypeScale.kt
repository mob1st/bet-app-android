package br.com.mob1st.core.design.atoms.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlin.math.pow

/**
 * The progression of font sizes used in the app typography.
 * It calculates the font size based on a given [base] text unit and a ratio.
 * @property base the base text size unit to be used to calculate the font size.
 * @property fontSizeRatio the ratio to be used to calculate the font size. The default value is the Major Second.
 * @property lineHeightRatio the ratio to be used to calculate the line height.
 * @see [https://supercharge.design/blog/what-is-a-type-scale]
 */
class FontTypeScale(
    private val base: TextUnit = baseTextUnit,
    private val fontSizeRatio: Float = MAJOR_SECOND_FONT_SCALE,
    private val lineHeightRatio: Float = LINE_HEIGHT_RATIO,
    private val letterSpacingRatio: Double = LETTER_SPACING_RATIO,
) {
    /**
     * Creates a [TextStyle] using the given parameters and calculating the font size and line height with the scale
     * ratio and the base text unit.
     * @param fontFamily the font family to be used.
     * @param fontWeight the font weight to be used.
     * @param sizePower the power to be used to calculate the font size. It's applied to the [fontSizeRatio] before
     * multiplying it by the [base].
     */
    fun style(
        fontFamily: FontFamily,
        fontWeight: FontWeight,
        sizePower: Int,
    ): TextStyle {
        val fontSize = base * (fontSizeRatio.pow(sizePower))
        val lineHeight = fontSize * lineHeightRatio
        val letterSpacing = (letterSpacingRatio / fontSize.value).em
        return TextStyle(
            fontFamily = fontFamily,
            fontSize = fontSize,
            fontWeight = fontWeight,
            lineHeight = lineHeight,
            letterSpacing = letterSpacing,
        )
    }

    companion object {
        private val baseTextUnit = 14.sp
        private const val MAJOR_SECOND_FONT_SCALE = 1.125f
        private const val LINE_HEIGHT_RATIO = 1.2f
        private const val LETTER_SPACING_RATIO = 0.2
    }
}
