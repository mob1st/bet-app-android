package br.com.mob1st.core.design.atoms.colors.material

import br.com.mob1st.core.design.atoms.colors.contrast.UiContrast
import br.com.mob1st.core.design.atoms.colors.material.families.ContrastedColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.FamilyThemeVariator

/**
 * Provides the two variations of the contrasted color families based on the given [UiContrast].
 * @property uiContrast the contrast level.
 */
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

    /**
     * Provides the specific contrasted families to a single theme variation.
     * It's expected to have only two implementations of this interface: one for the light theme and another for the
     * dark theme.
     */
    sealed interface ContrastVariation {

        /**
         * Provides the colors when the devices has the standard contrast setting.
         * @return the contrasted color families.
         */
        fun standard(): ContrastedColorFamilies

        /**
         * Provides the colors when the devices has the medium contrast setting.
         * @return the contrasted color families.
         */
        fun medium(): ContrastedColorFamilies

        /**
         * Provides the colors when the devices has the high contrast setting.
         * @return the contrasted color families.
         */
        fun high(): ContrastedColorFamilies
    }
}
