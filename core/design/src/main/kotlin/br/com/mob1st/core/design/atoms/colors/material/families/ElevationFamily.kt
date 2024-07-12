package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color
import br.com.mob1st.core.design.atoms.colors.tonals.BlackTonal

internal data class ElevationFamily(
    val scrim: Color,
) {
    companion object : FamilyThemeVariator<ElevationFamily> {
        override fun light() = ElevationFamily(
            scrim = BlackTonal.x1,
        )

        override fun dark() = ElevationFamily(
            scrim = BlackTonal.x1,
        )
    }
}
