package br.com.mob1st.core.design.atoms.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import br.com.mob1st.core.design.R

@Immutable
internal data class FontCategory(
    val small: TextStyle,
    val medium: TextStyle,
    val large: TextStyle,
) {
    companion object {
        fun Display() =
            FontCategory(
                small =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
                medium =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
                large =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
            )

        fun Headline() =
            FontCategory(
                small =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
                medium =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
                large =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
            )

        fun Title() =
            FontCategory(
                small =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.ExtraLight,
                        fontStyle = FontStyle.Normal,
                    ),
                medium =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.ExtraLight,
                        fontStyle = FontStyle.Normal,
                    ),
                large =
                    TextStyle(
                        fontFamily = robotoFlex,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
            )

        fun Body() =
            FontCategory(
                small =
                    TextStyle(
                        fontFamily = robotoSerif,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
                medium =
                    TextStyle(
                        fontFamily = robotoSerif,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
                large =
                    TextStyle(
                        fontFamily = robotoSerif,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
            )

        fun Label() =
            FontCategory(
                small =
                    TextStyle(
                        fontFamily = robotoSerif,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
                medium =
                    TextStyle(
                        fontFamily = robotoSerif,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
                large =
                    TextStyle(
                        fontFamily = robotoSerif,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Normal,
                    ),
            )
    }
}

@OptIn(ExperimentalTextApi::class)
@Suppress("MagicNumber")
internal val robotoFlex =
    FontFamily(
        Font(
            resId = R.font.roboto_serif,
            weight = FontWeight.Thin,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.width(300f),
                ),
        ),
        Font(
            resId = R.font.roboto_serif,
            weight = FontWeight.ExtraLight,
        ),
        Font(
            resId = R.font.roboto_serif,
            weight = FontWeight.Light,
        ),
        Font(
            resId = R.font.roboto_serif,
            weight = FontWeight.Normal,
        ),
        Font(
            resId = R.font.roboto_serif,
            weight = FontWeight.Medium,
        ),
    )

@Suppress("MagicNumber")
internal val robotoSerif =
    FontFamily(
        Font(
            resId = R.font.roboto_serif,
            weight = FontWeight.Thin,
        ),
    )
