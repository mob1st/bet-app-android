package br.com.mob1st.core.design.atoms.typography

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import br.com.mob1st.core.design.R

@OptIn(ExperimentalTextApi::class)
internal val montserrat = FontFamily(
    Font(
        R.font.montserrat_flex,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Normal.weight),
        ),
    ),
    Font(
        R.font.montserrat_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.SemiBold.weight),
        ),
    ),
    Font(
        R.font.montserrat_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Bold.weight),
        ),
    ),
)

@OptIn(ExperimentalTextApi::class)
internal val notoSansFlex = FontFamily(
    Font(
        R.font.noto_sans_flex,
        variationSettings = FontVariation.Settings(FontWeight.Light, FontStyle.Normal),
    ),
    Font(
        R.font.noto_sans_flex,
        variationSettings = FontVariation.Settings(FontWeight.Normal, FontStyle.Normal),
    ),
    Font(
        R.font.noto_sans_flex,
        variationSettings = FontVariation.Settings(FontWeight.Medium, FontStyle.Normal),
    ),
    Font(
        R.font.noto_sans_flex,
        variationSettings = FontVariation.Settings(FontWeight.SemiBold, FontStyle.Normal),
    ),
    Font(
        R.font.noto_sans_flex,
        variationSettings = FontVariation.Settings(FontWeight.Bold, FontStyle.Normal),
    ),
    Font(
        R.font.noto_sans_flex,
        variationSettings = FontVariation.Settings(FontWeight.ExtraBold, FontStyle.Normal),
    ),
    Font(
        R.font.noto_sans_flex,
        variationSettings = FontVariation.Settings(FontWeight.Black, FontStyle.Normal),
    ),
)
