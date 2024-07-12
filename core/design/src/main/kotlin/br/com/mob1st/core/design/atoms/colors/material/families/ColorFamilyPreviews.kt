package br.com.mob1st.core.design.atoms.colors.material.families

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.colors.material.LightContrastVariation
import br.com.mob1st.core.design.atoms.colors.material.NightContrastVariation
import br.com.mob1st.core.design.atoms.spacing.Spacings

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 1000,
)
private fun LightStandardContrastPreview(
    @PreviewParameter(LightContrastedColorFamilyProvider::class) families: ContrastedColorFamilies,
) {
    StandardContrastPreview(families = families)
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 1000,
    backgroundColor = 0xFF101010,
)
private fun NightStandardContrastPreview(
    @PreviewParameter(NightContrastedColorFamilyProvider::class) families: ContrastedColorFamilies,
) {
    StandardContrastPreview(families = families)
}

@Composable
private fun StandardContrastPreview(
    families: ContrastedColorFamilies,
) {
    Row(
        modifier = Modifier.padding(Spacings.x4),
        horizontalArrangement = Arrangement.spacedBy(Spacings.x8),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacings.x4),
        ) {
            ColorFamilyItem(name = "Primary", colorFamily = families.primary)
            ColorFamilyItem(name = "Secondary", colorFamily = families.secondary)
            ColorFamilyItem(name = "Tertiary", colorFamily = families.tertiary)
            ColorFamilyItem(name = "Error", colorFamily = families.error)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacings.x4),
        ) {
            ColorFamilyItem(name = "Incomes", colorFamily = families.extensions.incomes)
            ColorFamilyItem(name = "Fixed Expenses", colorFamily = families.extensions.fixedExpenses)
            ColorFamilyItem(name = "Variable Expenses", colorFamily = families.extensions.variableExpenses)
            ColorFamilyItem(name = "Seasonal Expenses", colorFamily = families.extensions.seasonalExpenses)
        }
    }
}

@Composable
private fun ColorFamilyItem(
    name: String,
    colorFamily: ColorFamily,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .background(colorFamily.color)
                    .topColorSize(),
            ) {
                Text(
                    text = name,
                    style = LocalTextStyle.current.copy(color = colorFamily.onColor),
                )
            }
            Box(
                modifier = Modifier
                    .background(colorFamily.onColor)
                    .bottomColorSize(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "On $name",
                    style = LocalTextStyle.current.copy(color = colorFamily.color),
                )
            }
        }
        Spacer(modifier = Modifier.width(Spacings.x4))
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .background(colorFamily.container)
                    .topColorSize(),
            ) {
                Text(
                    text = "$name\nContainer",
                    style = LocalTextStyle.current.copy(color = colorFamily.onContainer),
                )
            }
            Box(
                modifier = Modifier
                    .background(colorFamily.onContainer)
                    .bottomColorSize(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "On $name Container",
                    style = LocalTextStyle.current.copy(color = colorFamily.container),
                )
            }
        }
    }
}

private fun Modifier.topColorSize() = height(128.dp)
    .fillMaxWidth()
    .padding(Spacings.x4)

private fun Modifier.bottomColorSize() = height(48.dp)
    .fillMaxWidth()
    .padding(Spacings.x4)

internal class LightContrastedColorFamilyProvider : PreviewParameterProvider<ContrastedColorFamilies> {
    override val values: Sequence<ContrastedColorFamilies> = sequenceOf(
        LightContrastVariation.standard(),
        LightContrastVariation.medium(),
        LightContrastVariation.high(),
    )
}

internal class NightContrastedColorFamilyProvider : PreviewParameterProvider<ContrastedColorFamilies> {
    override val values: Sequence<ContrastedColorFamilies> = sequenceOf(
        NightContrastVariation.standard(),
        NightContrastVariation.medium(),
        NightContrastVariation.high(),
    )
}
