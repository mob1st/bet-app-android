package br.com.mob1st.core.design.atoms.typography

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.utils.ThemedPreview

/**
 * Wraps the font roles to be used in numeric texts.
 */
object NumericFontRoles : Material3Roles {
    private val scale = FontTypeScale()

    override val display = FontRole(
        small = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Bold,
            sizePower = 8,
        ),
        medium = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Bold,
            sizePower = 10,
        ),
        large = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Bold,
            sizePower = 12,
        ),
    )
    override val headline = FontRole(
        small = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Bold,
            sizePower = 5,
        ),
        medium = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Bold,
            sizePower = 6,
        ),
        large = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Bold,
            sizePower = 7,
        ),
    )
    override val title = FontRole(
        small = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Bold,
            sizePower = 0,
        ),
        medium = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.SemiBold,
            sizePower = 1,
        ),
        large = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.SemiBold,
            sizePower = 3,
        ),
    )
    override val body = FontRole(
        small = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Normal,
            sizePower = -1,
        ),
        medium = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Normal,
            sizePower = 0,
        ),
        large = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Normal,
            sizePower = 1,
        ),
    )
    override val label = FontRole(
        small = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.Bold,
            sizePower = -2,
        ),
        medium = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.SemiBold,
            sizePower = -1,
        ),
        large = scale.style(
            fontFamily = titilliumWeb,
            fontWeight = FontWeight.SemiBold,
            sizePower = 0,
        ),
    )
}

@Composable
@ThemedPreview
private fun NumericFontRolesPreview() {
    TwoCentsTheme {
        Column {
            Text("$1,234,567.89%", style = NumericFontRoles.display.large)
            Text("$1,234,567.89%", style = NumericFontRoles.display.medium)
            Text("$1,234,567.89%", style = NumericFontRoles.display.small)
            Text("$1,234,567.89%", style = NumericFontRoles.headline.large)
            Text("$1,234,567.89%", style = NumericFontRoles.headline.medium)
            Text("$1,234,567.89%", style = NumericFontRoles.headline.small)
            Text("$1,234,567.89%", style = NumericFontRoles.title.large)
            Text("$1,234,567.89%", style = NumericFontRoles.title.medium)
            Text("$1,234,567.89%", style = NumericFontRoles.title.small)
            Text("$1,234,567.89%", style = NumericFontRoles.body.large)
            Text("$1,234,567.89%", style = NumericFontRoles.body.medium)
            Text("$1,234,567.89%", style = NumericFontRoles.body.small)
            Text("$1,234,567.89%", style = NumericFontRoles.label.large)
            Text("$1,234,567.89%", style = NumericFontRoles.label.medium)
            Text("$1,234,567.89%", style = NumericFontRoles.label.small)
        }
    }
}
