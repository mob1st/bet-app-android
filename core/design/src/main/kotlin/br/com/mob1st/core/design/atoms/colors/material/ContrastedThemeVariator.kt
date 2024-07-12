package br.com.mob1st.core.design.atoms.colors.material

import br.com.mob1st.core.design.atoms.colors.contrast.UiContrast
import br.com.mob1st.core.design.atoms.colors.material.families.ContrastedColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.FamilyThemeVariator

internal class ContrastedThemeVariator(
    private val uiContrast: UiContrast,
) : FamilyThemeVariator<ContrastedColorFamilies> {
    override fun light(): ContrastedColorFamilies {
        return when (uiContrast) {
            UiContrast.STANDARD -> LightContrastVariation.standard()
            UiContrast.MEDIUM -> LightContrastVariation.medium()
            UiContrast.HIGH -> LightContrastVariation.high()
        }
    }

    override fun dark(): ContrastedColorFamilies {
        return when (uiContrast) {
            UiContrast.STANDARD -> LightContrastVariation.standard()
            UiContrast.MEDIUM -> LightContrastVariation.medium()
            UiContrast.HIGH -> LightContrastVariation.high()
        }
    }

    interface ContrastVariation {
        fun standard(): ContrastedColorFamilies

        fun medium(): ContrastedColorFamilies

        fun high(): ContrastedColorFamilies
    }
}
