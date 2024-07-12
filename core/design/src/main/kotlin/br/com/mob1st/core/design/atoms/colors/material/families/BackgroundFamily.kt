package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color
import br.com.mob1st.core.design.atoms.colors.tonals.BlackTonal
import br.com.mob1st.core.design.atoms.colors.tonals.WhiteTonal

data class BackgroundFamily(
    val background: Color,
    val onBackground: Color,
) {
    companion object : FamilyThemeVariator<BackgroundFamily> {
        override fun light(): BackgroundFamily = BackgroundFamily(
            background = WhiteTonal.x5,
            onBackground = BlackTonal.x2,
        )

        override fun dark(): BackgroundFamily = BackgroundFamily(
            background = BlackTonal.x2,
            onBackground = WhiteTonal.x6,
        )
    }
}
