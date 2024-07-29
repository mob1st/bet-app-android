package br.com.mob1st.core.design.molecules.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.colors.material.LocalExtensionsColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.LocalMaterialColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.ColorCombination
import br.com.mob1st.core.design.atoms.icons.FixedExpensesIcon
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview

/**
 * Icon component to be used as leading in List items
 * @param combination The color combination to be used for the background and the icon tint/filter.
 * @param scale The scale of the icon.
 * @param iconContent The content of the icon.
 */
@Composable
fun LeadingIcon(
    modifier: Modifier = Modifier,
    combination: ColorCombination = LocalMaterialColorFamilies.current.backgroundFamily.containerCombination,
    scale: IconScale = IconScale.Large,
    iconContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(color = combination.background)
            .size(scale.boxSize)
            .padding(scale.boxSize / 4),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides combination.content) {
            iconContent()
        }
    }
}

/**
 * The scale used in [LeadingIcon].
 */
enum class IconScale(val boxSize: Dp) {
    Small(boxSize = 24.dp),
    Large(boxSize = 40.dp),
}

@Composable
@ThemedPreview
private fun LeadingIconPreview() {
    PreviewTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacings.x2),
        ) {
            LeadingIcon(
                combination = LocalExtensionsColorFamilies.current.fixedExpenses.containerCombination,
                scale = IconScale.Small,
            ) {
                FixedExpensesIcon(contentDescription = "small fixed icons")
            }
            LeadingIcon(
                combination = LocalExtensionsColorFamilies.current.fixedExpenses.containerCombination,
                scale = IconScale.Large,
            ) {
                FixedExpensesIcon(contentDescription = "large fixed icons")
            }
        }
    }
}
