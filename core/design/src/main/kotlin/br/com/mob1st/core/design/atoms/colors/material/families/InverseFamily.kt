package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color
import br.com.mob1st.core.design.atoms.colors.tonals.BlackTonal
import br.com.mob1st.core.design.atoms.colors.tonals.WhiteTonal

internal data class InverseFamily(
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val inversePrimary: Color,
) {
    companion object : FamilyThemeVariator<InverseFamily> {
        override fun light(): InverseFamily = InverseFamily(
            inverseSurface = BlackTonal.x6,
            inverseOnSurface = WhiteTonal.x2,
            inversePrimary = WhiteTonal.x6,
        )

        override fun dark(): InverseFamily = InverseFamily(
            inverseSurface = WhiteTonal.x1,
            inverseOnSurface = BlackTonal.x6,
            inversePrimary = BlackTonal.x1,
        )
    }
}
