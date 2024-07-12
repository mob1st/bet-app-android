package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color

internal data class SurfaceFamily(
    val surface: Color,
    val dim: Color,
    val bright: Color,
    val containerLowest: Color,
    val containerLow: Color,
    val container: Color,
    val containerHigh: Color,
    val containerHighest: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val outlineVariant: Color,
) {
    companion object : FamilyThemeVariator<SurfaceFamily> {
        override fun light() = SurfaceFamily(
            surface = Color.White,
            dim = Color(0xFFF5F5F5),
            bright = Color(0xFFE0E0E0),
            containerLowest = Color(0xFFFAFAFA),
            containerLow = Color(0xFFF5F5F5),
            container = Color(0xFFEEEEEE),
            containerHigh = Color(0xFFE0E0E0),
            containerHighest = Color(0xFFD6D6D6),
            onSurface = Color.Black,
            onSurfaceVariant = Color(0xFF757575),
            outline = Color(0xFFBDBDBD),
            outlineVariant = Color(0xFF9E9E9E),
        )

        override fun dark() = SurfaceFamily(
            surface = Color(0xFF121212),
            dim = Color(0xFF1E1E1E),
            bright = Color(0xFF333333),
            containerLowest = Color(0xFF1C1C1C),
            containerLow = Color(0xFF212121),
            container = Color(0xFF242424),
            containerHigh = Color(0xFF2C2C2C),
            containerHighest = Color(0xFF333333),
            onSurface = Color.White,
            onSurfaceVariant = Color(0xFFBDBDBD),
            outline = Color(0xFF9E9E9E),
            outlineVariant = Color(0xFF757575),
        )
    }
}
