package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color

internal data class InverseFamily(
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val inversePrimary: Color,
) {
    companion object : FamilyThemeVariator<InverseFamily> {
        override fun light(): InverseFamily {
            TODO("Not yet implemented")
        }

        override fun dark(): InverseFamily {
            TODO("Not yet implemented")
        }
    }
}
