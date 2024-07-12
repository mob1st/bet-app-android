package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.ui.graphics.Color

data class BackgroundFamily(
    val background: Color,
    val onBackground: Color,
) {
    companion object : FamilyThemeVariator<BackgroundFamily> {
        override fun light(): BackgroundFamily {
            TODO("Not yet implemented")
        }

        override fun dark(): BackgroundFamily {
            TODO("Not yet implemented")
        }
    }
}
