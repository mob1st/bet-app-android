package br.com.mob1st.core.design.atoms.typography

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import br.com.mob1st.core.design.R

internal val montserrat = FontFamily(
    Font(
        R.font.montserrat__regular,
        weight = FontWeight.Normal,
    ),
    Font(
        R.font.montserrat__medium,
        weight = FontWeight.Medium,
    ),
    Font(
        R.font.montserrat__semi_bold,
        weight = FontWeight.SemiBold,
    ),
    Font(
        R.font.montserrat__bold,
        weight = FontWeight.Bold,
    ),
    Font(
        R.font.montserrat__extra_bold,
        weight = FontWeight.ExtraBold,
    ),
    Font(
        R.font.montserrat__black,
        weight = FontWeight.Black,
    ),
)

internal val notoSans = FontFamily(
    Font(
        R.font.noto_sans__light,
        weight = FontWeight.Light,
    ),
    Font(
        R.font.noto_sans__regular,
        weight = FontWeight.Normal,
    ),
    Font(
        R.font.noto_sans__medium,
        weight = FontWeight.Medium,
    ),
    Font(
        R.font.noto_sans__semi_bold,
        weight = FontWeight.SemiBold,
    ),
    Font(
        R.font.noto_sans__bold,
        weight = FontWeight.Bold,
    ),
    Font(
        R.font.noto_sans__extra_bold,
        weight = FontWeight.ExtraBold,
    ),
    Font(
        R.font.noto_sans__black,
        weight = FontWeight.Black,
    ),
)
