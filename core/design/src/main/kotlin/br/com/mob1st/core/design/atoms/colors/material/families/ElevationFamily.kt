package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color

internal data class ElevationFamily(
    val scrim: Color,
) {
    companion object : FamilyThemeVariator<ElevationFamily> {
        override fun light(): ElevationFamily {
            TODO("Not yet implemented")
        }

        override fun dark(): ElevationFamily {
            TODO("Not yet implemented")
        }
    }
}
