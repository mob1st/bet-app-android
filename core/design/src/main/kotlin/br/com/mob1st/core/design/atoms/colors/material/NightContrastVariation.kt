package br.com.mob1st.core.design.atoms.colors.material

import br.com.mob1st.core.design.atoms.colors.material.families.ColorFamily
import br.com.mob1st.core.design.atoms.colors.material.families.ContrastedColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.TwoCentsColorExtension
import br.com.mob1st.core.design.atoms.colors.tonals.AmaranthPinkTonal
import br.com.mob1st.core.design.atoms.colors.tonals.BlackTonal
import br.com.mob1st.core.design.atoms.colors.tonals.JasperTonal
import br.com.mob1st.core.design.atoms.colors.tonals.MauveTonal
import br.com.mob1st.core.design.atoms.colors.tonals.NyanzaTonal
import br.com.mob1st.core.design.atoms.colors.tonals.UranianBlueTonal
import br.com.mob1st.core.design.atoms.colors.tonals.VanillaTonal
import br.com.mob1st.core.design.atoms.colors.tonals.WhiteTonal

/**
 * Night contrast variation for the color scheme.
 */
data object NightContrastVariation : ContrastedThemeVariator.ContrastVariation {
    override fun standard(): ContrastedColorFamilies {
        return ContrastedColorFamilies(
            primary = ColorFamily(
                color = WhiteTonal.x6,
                onColor = BlackTonal.x1,
                container = WhiteTonal.x6,
                onContainer = BlackTonal.x1,
            ),
            secondary = ColorFamily(
                color = UranianBlueTonal.x80,
                onColor = UranianBlueTonal.x20,
                container = UranianBlueTonal.x95,
                onContainer = UranianBlueTonal.x10,
            ),
            tertiary = ColorFamily(
                color = UranianBlueTonal.x80,
                onColor = UranianBlueTonal.x20,
                container = UranianBlueTonal.x95,
                onContainer = UranianBlueTonal.x10,
            ),
            error = ColorFamily(
                color = JasperTonal.x80,
                onColor = JasperTonal.x20,
                container = JasperTonal.x95,
                onContainer = JasperTonal.x10,
            ),
            extensions = TwoCentsColorExtension(
                incomes = ColorFamily(
                    color = NyanzaTonal.x80,
                    onColor = NyanzaTonal.x20,
                    container = NyanzaTonal.x95,
                    onContainer = NyanzaTonal.x10,
                ),
                fixedExpenses = ColorFamily(
                    color = AmaranthPinkTonal.x80,
                    onColor = AmaranthPinkTonal.x20,
                    container = AmaranthPinkTonal.x95,
                    onContainer = AmaranthPinkTonal.x10,
                ),
                variableExpenses = ColorFamily(
                    color = VanillaTonal.x80,
                    onColor = VanillaTonal.x20,
                    container = VanillaTonal.x95,
                    onContainer = VanillaTonal.x10,
                ),
                seasonalExpenses = ColorFamily(
                    color = MauveTonal.x80,
                    onColor = MauveTonal.x20,
                    container = MauveTonal.x95,
                    onContainer = MauveTonal.x10,
                ),
            ),
        )
    }

    override fun medium(): ContrastedColorFamilies {
        return ContrastedColorFamilies(
            primary = ColorFamily(
                color = WhiteTonal.x6,
                onColor = BlackTonal.x1,
                container = WhiteTonal.x6,
                onContainer = BlackTonal.x1,
            ),
            secondary = ColorFamily(
                color = UranianBlueTonal.x90,
                onColor = UranianBlueTonal.x30,
                container = UranianBlueTonal.x99,
                onContainer = UranianBlueTonal.x10,
            ),
            tertiary = ColorFamily(
                color = UranianBlueTonal.x90,
                onColor = UranianBlueTonal.x30,
                container = UranianBlueTonal.x99,
                onContainer = UranianBlueTonal.x10,
            ),
            error = ColorFamily(
                color = JasperTonal.x90,
                onColor = JasperTonal.x30,
                container = JasperTonal.x99,
                onContainer = JasperTonal.x10,
            ),
            extensions = TwoCentsColorExtension(
                incomes = ColorFamily(
                    color = NyanzaTonal.x90,
                    onColor = NyanzaTonal.x30,
                    container = NyanzaTonal.x99,
                    onContainer = NyanzaTonal.x10,
                ),
                fixedExpenses = ColorFamily(
                    color = AmaranthPinkTonal.x90,
                    onColor = AmaranthPinkTonal.x30,
                    container = AmaranthPinkTonal.x99,
                    onContainer = AmaranthPinkTonal.x10,
                ),
                variableExpenses = ColorFamily(
                    color = VanillaTonal.x90,
                    onColor = VanillaTonal.x30,
                    container = VanillaTonal.x99,
                    onContainer = VanillaTonal.x10,
                ),
                seasonalExpenses = ColorFamily(
                    color = MauveTonal.x90,
                    onColor = MauveTonal.x30,
                    container = MauveTonal.x95,
                    onContainer = MauveTonal.x10,
                ),
            ),
        )
    }

    override fun high(): ContrastedColorFamilies {
        return ContrastedColorFamilies(
            primary = ColorFamily(
                color = WhiteTonal.x6,
                onColor = BlackTonal.x1,
                container = WhiteTonal.x6,
                onContainer = BlackTonal.x1,
            ),
            secondary = ColorFamily(
                color = UranianBlueTonal.x95,
                onColor = UranianBlueTonal.x30,
                container = UranianBlueTonal.x99,
                onContainer = UranianBlueTonal.x10,
            ),
            tertiary = ColorFamily(
                color = UranianBlueTonal.x95,
                onColor = UranianBlueTonal.x30,
                container = UranianBlueTonal.x99,
                onContainer = UranianBlueTonal.x10,
            ),
            error = ColorFamily(
                color = JasperTonal.x95,
                onColor = JasperTonal.x30,
                container = JasperTonal.x99,
                onContainer = JasperTonal.x10,
            ),
            extensions = TwoCentsColorExtension(
                incomes = ColorFamily(
                    color = NyanzaTonal.x95,
                    onColor = NyanzaTonal.x30,
                    container = NyanzaTonal.x99,
                    onContainer = NyanzaTonal.x10,
                ),
                fixedExpenses = ColorFamily(
                    color = AmaranthPinkTonal.x95,
                    onColor = AmaranthPinkTonal.x30,
                    container = AmaranthPinkTonal.x99,
                    onContainer = AmaranthPinkTonal.x10,
                ),
                variableExpenses = ColorFamily(
                    color = VanillaTonal.x95,
                    onColor = VanillaTonal.x30,
                    container = VanillaTonal.x99,
                    onContainer = VanillaTonal.x10,
                ),
                seasonalExpenses = ColorFamily(
                    color = MauveTonal.x95,
                    onColor = MauveTonal.x30,
                    container = MauveTonal.x95,
                    onContainer = MauveTonal.x10,
                ),
            ),
        )
    }
}
