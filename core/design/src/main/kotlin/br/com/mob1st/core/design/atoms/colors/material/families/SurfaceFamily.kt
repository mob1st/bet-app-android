package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color
import br.com.mob1st.core.design.atoms.colors.tonals.BlackTonal
import br.com.mob1st.core.design.atoms.colors.tonals.GreyTonal
import br.com.mob1st.core.design.atoms.colors.tonals.WhiteTonal

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
            dim = WhiteTonal.x1,
            surface = WhiteTonal.x5,
            bright = WhiteTonal.x5,
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
            containerLowest = WhiteTonal.x6,
            containerLow = WhiteTonal.x5,
            container = WhiteTonal.x4,
            containerHigh = WhiteTonal.x3,
            containerHighest = WhiteTonal.x1,
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
            onSurface = BlackTonal.x2,
            onSurfaceVariant = GreyTonal.x1,
            outline = GreyTonal.x4,
            outlineVariant = GreyTonal.x5,
        )

        override fun dark() = SurfaceFamily(
            surface = BlackTonal.x2,
            dim = BlackTonal.x2,
            bright = BlackTonal.x6,
            containerLowest = BlackTonal.x1,
            containerLow = BlackTonal.x3,
            container = BlackTonal.x4,
            containerHigh = BlackTonal.x5,
            containerHighest = BlackTonal.x6,
            onSurface = WhiteTonal.x1,
            onSurfaceVariant = GreyTonal.x6,
            outline = GreyTonal.x4,
            outlineVariant = GreyTonal.x1,
        )
    }
}
