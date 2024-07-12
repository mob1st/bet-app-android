package br.com.mob1st.core.design.atoms.colors.material.families

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.spacing.Spacings

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 1000,
    name = "Dark Neutral Family",
)
private fun LightSurfaceFamilyPreviews() {
    SurfaceFamilyView(
        surfaceFamily = SurfaceFamily.light(),
        mode = Color.White,
        inverted = Color.Black,
    )
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF121212,
    widthDp = 1000,
    name = "Dark Neutral Family",
)
private fun NightSurfaceFamilyPreviews() {
    SurfaceFamilyView(
        surfaceFamily = SurfaceFamily.dark(),
        mode = Color.Black,
        inverted = Color.White,
    )
}

@Composable
@Suppress("LongMethod")
private fun SurfaceFamilyView(
    surfaceFamily: SurfaceFamily,
    mode: Color,
    inverted: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacings.x4),
    ) {
        Row(modifier = Modifier.weight(2f)) {
            ColorSpace(Modifier.weight(1f), background = surfaceFamily.dim, isTopColor = true) {
                Text(text = "Dim", color = inverted)
            }
            ColorSpace(Modifier.weight(1f), background = surfaceFamily.surface, isTopColor = true) {
                Text(text = "Surface", color = inverted)
            }
            ColorSpace(Modifier.weight(1f), background = surfaceFamily.bright, isTopColor = true) {
                Text(text = "Bright", color = inverted)
            }
        }
        Row(modifier = Modifier.weight(2f)) {
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.containerLowest,
                isTopColor = true,
            ) {
                Text(text = "Lowest Container", color = inverted)
            }
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.containerLow,
                isTopColor = true,
            ) {
                Text(text = "Low Container", color = inverted)
            }
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.container,
                isTopColor = true,
            ) {
                Text(text = "Container", color = inverted)
            }
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.containerHigh,
                isTopColor = true,
            ) {
                Text(text = "High Container", color = inverted)
            }
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.containerHighest,
                isTopColor = true,
            ) {
                Text(text = "Highest Container", color = inverted)
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.onSurface,
                isTopColor = false,
            ) {
                Text(text = "On Surface", color = mode)
            }
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.onSurfaceVariant,
                isTopColor = false,
            ) {
                Text(text = "On Surface Var.", color = mode)
            }
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.outline,
                isTopColor = false,
            ) {
                Text(text = "Outline", color = mode)
            }
            ColorSpace(
                Modifier.weight(1f),
                background = surfaceFamily.outlineVariant,
                isTopColor = false,
            ) {
                Text(text = "Outline Variant", color = inverted)
            }
        }
    }
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "Light Inv Bg Elev Family",
)
private fun LightInverseBgAndElevationFamilyView() {
    InverseBgAndElevationFamilyView(
        inverseFamily = InverseFamily.light(),
        backgroundFamily = BackgroundFamily.light(),
        elevationFamily = ElevationFamily.light(),
        mode = Color.White,
        inverse = Color.Black,
    )
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF121212,
    name = "Night Inv Bg Elev Family",
)
private fun NightInverseBgAndElevationFamilyView() {
    InverseBgAndElevationFamilyView(
        inverseFamily = InverseFamily.dark(),
        backgroundFamily = BackgroundFamily.dark(),
        elevationFamily = ElevationFamily.dark(),
        mode = Color.Black,
        inverse = Color.White,
    )
}

@Composable
private fun InverseBgAndElevationFamilyView(
    inverseFamily: InverseFamily,
    backgroundFamily: BackgroundFamily,
    elevationFamily: ElevationFamily,
    mode: Color,
    inverse: Color,
) {
    Column(
        modifier = Modifier
            .width(360.dp)
            .padding(Spacings.x4),
    ) {
        ColorSpace(
            modifier = Modifier.weight(3f),
            background = inverseFamily.inverseSurface,
            isTopColor = true,
        ) {
            Text(text = "Inverse Surface", color = mode)
        }

        ColorSpace(
            modifier = Modifier.weight(1f),
            background = inverseFamily.inverseOnSurface,
            isTopColor = true,
        ) {
            Text(text = "Inverse On Surface", color = inverse)
        }

        ColorSpace(
            modifier = Modifier.weight(1f),
            background = inverseFamily.inversePrimary,
            isTopColor = true,
        ) {
            Text(text = "Inverse Primary", color = inverse)
        }

        ColorSpace(
            modifier = Modifier.weight(1f),
            background = backgroundFamily.background,
            isTopColor = true,
        ) {
            Text(text = "Background", color = inverse)
        }

        ColorSpace(
            modifier = Modifier.weight(1f),
            background = backgroundFamily.onBackground,
            isTopColor = true,
        ) {
            Text(text = "On Background", color = mode)
        }

        ColorSpace(
            modifier = Modifier.weight(1f),
            background = elevationFamily.scrim,
            isTopColor = true,
        ) {
            Text(text = "Scrim", color = Color.White)
        }
    }
}

@Composable
private fun ColorSpace(
    modifier: Modifier,
    background: Color,
    isTopColor: Boolean,
    text: @Composable BoxScope.() -> Unit,
) {
    val alignment = if (isTopColor) {
        Alignment.TopStart
    } else {
        Alignment.CenterStart
    }
    Box(
        modifier = modifier
            .background(background)
            .fillMaxSize()
            .padding(Spacings.x4),
        contentAlignment = alignment,
        content = text,
    )
}
