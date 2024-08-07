package br.com.mob1st.core.design.atoms.typography

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.utils.ThemedPreview

/**
 * Wraps the default font roles for texts.
 * It provides all the standard roles for Material3 typography.
 * @see [https://m3.material.io/styles/typography/applying-type]
 */
data object DefaultFontRoles : Material3Roles {
    private val scale = FontTypeScale()

    override val display = FontRole(
        small = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            sizePower = 8,
        ),
        medium = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.ExtraBold,
            sizePower = 10,
        ),
        large = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.Black,
            sizePower = 12,
        ),
    )
    override val headline = FontRole(
        small = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            sizePower = 5,
        ),
        medium = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            sizePower = 6,
        ),
        large = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.ExtraBold,
            sizePower = 7,
        ),
    )
    override val title = FontRole(
        small = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.Black,
            sizePower = -1,
        ),
        medium = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            sizePower = 1,
        ),
        large = scale.style(
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            sizePower = 4,
        ),
    )
    override val body = FontRole(
        small = scale.style(
            fontFamily = notoSans,
            fontWeight = FontWeight.Normal,
            sizePower = -1,
        ),
        medium = scale.style(
            fontFamily = notoSans,
            fontWeight = FontWeight.Medium,
            sizePower = 0,
        ),
        large = scale.style(
            fontFamily = notoSans,
            fontWeight = FontWeight.Medium,
            sizePower = 2,
        ),
    )

    override val label = FontRole(
        small = scale.style(
            fontFamily = notoSans,
            fontWeight = FontWeight.Light,
            sizePower = -2,
        ),
        medium = scale.style(
            fontFamily = notoSans,
            fontWeight = FontWeight.Bold,
            sizePower = -1,
        ),
        large = scale.style(
            fontFamily = notoSans,
            fontWeight = FontWeight.Bold,
            sizePower = 0,
        ),
    )
}

@Composable
@ThemedPreview
fun DefaultFontRolesPreview() {
    val roles = DefaultFontRoles
    TwoCentsTheme {
        Column {
            Text(text = "Display Large ${roles.display.large.fontSize}", style = roles.display.large)
            Text(text = "Display Medium ${roles.display.medium.fontSize}", style = roles.display.medium)
            Text(text = "Display Small ${roles.display.small.fontSize}", style = roles.display.small)
            Text(text = "Headline Large ${roles.headline.large.fontSize}", style = roles.headline.large)
            Text(text = "Headline Medium ${roles.headline.medium.fontSize}", style = roles.headline.medium)
            Text(text = "Headline Small ${roles.headline.small.fontSize}", style = roles.headline.small)
            Text(text = "Title Large ${roles.title.large.fontSize}", style = roles.title.large)
            Text(text = "Title Medium ${roles.title.medium.fontSize}", style = roles.title.medium)
            Text(text = "Title Small ${roles.title.small.fontSize}", style = roles.title.small)
            Text(text = "Body Large ${roles.body.large.fontSize}", style = roles.body.large)
            Text(text = "Body Medium ${roles.body.medium.fontSize}", style = roles.body.medium)
            Text(text = "Body Small ${roles.body.small.fontSize}", style = roles.body.small)
            Text(text = "Label Large ${roles.label.large.fontSize}", style = roles.label.large)
            Text(text = "Label Medium ${roles.label.medium.fontSize}", style = roles.label.medium)
            Text(text = "Label Small ${roles.label.small.fontSize}", style = roles.label.small)
        }
    }
}
