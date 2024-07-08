package br.com.mob1st.core.design.atoms.typography

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
internal val openSans = FontFamily(
    Font(
        R.font.open_sans_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Normal.weight),
        ),
    ),
    Font(
        R.font.open_sans_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.SemiBold.weight),
        ),
    ),
    Font(
        R.font.open_sans_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Medium.weight),
        ),
    ),
    Font(
        R.font.montserrat_flex,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Bold.weight),
        ),
    ),
)

internal val titilliumWeb = FontFamily(
    Font(
        R.font.titillium_web_regular,
        weight = FontWeight.Normal,
    ),
    Font(
        R.font.titillium_web_semibold,
        weight = FontWeight.SemiBold,
    ),
    Font(
        R.font.titillium_web_semibold,
        weight = FontWeight.Bold,
    ),
)
