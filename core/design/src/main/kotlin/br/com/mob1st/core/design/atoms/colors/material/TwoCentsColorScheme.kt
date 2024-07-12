package br.com.mob1st.core.design.atoms.colors.material

import androidx.compose.material3.ColorScheme
import br.com.mob1st.core.design.atoms.colors.contrast.UiContrast
import br.com.mob1st.core.design.atoms.colors.material.families.BackgroundFamily
import br.com.mob1st.core.design.atoms.colors.material.families.ElevationFamily
import br.com.mob1st.core.design.atoms.colors.material.families.InverseFamily
import br.com.mob1st.core.design.atoms.colors.material.families.MaterialColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.SurfaceFamily
import br.com.mob1st.core.design.atoms.colors.material.families.TwoCentsColorExtension
import br.com.mob1st.core.design.atoms.colors.material.families.toDarkColorScheme
import br.com.mob1st.core.design.atoms.colors.material.families.toLightColorScheme

/**
 * Holds the color scheme used in the app.
 * It's an aggregation of the Material's [ColorScheme] and the extension colors required by the brand.
 * @property materialScheme The Material's color scheme.
 * @property extensions The extension colors required by the brand.
 */
internal data class TwoCentsColorScheme(
    val materialScheme: ColorScheme,
    val extensions: TwoCentsColorExtension,
) {
    companion object {
        /**
         * Factory method to create the [TwoCentsColorScheme].
         * @param isDark Whether the color scheme is dark or not.
         * @param uiContrast The contrast level defined in the device's settings.
         */
        fun create(isDark: Boolean, uiContrast: UiContrast): TwoCentsColorScheme {
            return if (isDark) {
                darkColorScheme(uiContrast)
            } else {
                lightColorScheme(uiContrast)
            }
        }

        private fun darkColorScheme(uiContrast: UiContrast): TwoCentsColorScheme {
            val themeVariator = ContrastedThemeVariator(uiContrast)
            val contrastedColorFamilies = themeVariator.dark()
            val materialColorFamilies = MaterialColorFamilies(
                primary = contrastedColorFamilies.primary,
                secondary = contrastedColorFamilies.secondary,
                tertiary = contrastedColorFamilies.tertiary,
                error = contrastedColorFamilies.error,
                surface = SurfaceFamily.dark(),
                backgroundFamily = BackgroundFamily.dark(),
                elevationFamily = ElevationFamily.dark(),
                inverseFamily = InverseFamily.dark(),
            )
            return TwoCentsColorScheme(
                materialScheme = materialColorFamilies.toDarkColorScheme(),
                extensions = contrastedColorFamilies.extensions,
            )
        }

        private fun lightColorScheme(uiContrast: UiContrast): TwoCentsColorScheme {
            val themeVariator = ContrastedThemeVariator(uiContrast)
            val contrastedColorFamilies = themeVariator.light()
            val materialColorFamilies = MaterialColorFamilies(
                primary = contrastedColorFamilies.primary,
                secondary = contrastedColorFamilies.secondary,
                tertiary = contrastedColorFamilies.tertiary,
                error = contrastedColorFamilies.error,
                surface = SurfaceFamily.light(),
                backgroundFamily = BackgroundFamily.light(),
                elevationFamily = ElevationFamily.light(),
                inverseFamily = InverseFamily.light(),
            )
            return TwoCentsColorScheme(
                materialScheme = materialColorFamilies.toLightColorScheme(),
                extensions = contrastedColorFamilies.extensions,
            )
        }
    }
}
