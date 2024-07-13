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
 * Light contrast variation for the color scheme.
 */
internal data object LightContrastVariation : ContrastedThemeVariator.ContrastVariation {
    override fun standard(): ContrastedColorFamilies = ContrastedColorFamilies(
        primary = ColorFamily(
            color = BlackTonal.x1,
            onColor = WhiteTonal.x6,
            container = WhiteTonal.x6,
            onContainer = BlackTonal.x1,
        ),
        secondary = ColorFamily(
            color = UranianBlueTonal.x50,
            onColor = UranianBlueTonal.x99,
            container = UranianBlueTonal.x90,
            onContainer = UranianBlueTonal.x10,
        ),
        tertiary = ColorFamily(
            color = UranianBlueTonal.x50,
            onColor = UranianBlueTonal.x99,
            container = UranianBlueTonal.x90,
            onContainer = UranianBlueTonal.x10,
        ),
        error = ColorFamily(
            color = JasperTonal.x50,
            onColor = JasperTonal.x99,
            container = JasperTonal.x90,
            onContainer = JasperTonal.x10,
        ),
        extensions = TwoCentsColorExtension(
            incomes = ColorFamily(
                color = NyanzaTonal.x50,
                onColor = NyanzaTonal.x99,
                container = NyanzaTonal.x90,
                onContainer = NyanzaTonal.x10,
            ),
            fixedExpenses = ColorFamily(
                color = AmaranthPinkTonal.x50,
                onColor = AmaranthPinkTonal.x99,
                container = AmaranthPinkTonal.x90,
                onContainer = AmaranthPinkTonal.x10,
            ),
            variableExpenses = ColorFamily(
                color = VanillaTonal.x50,
                onColor = VanillaTonal.x99,
                container = VanillaTonal.x90,
                onContainer = VanillaTonal.x10,
            ),
            seasonalExpenses = ColorFamily(
                color = MauveTonal.x50,
                onColor = MauveTonal.x99,
                container = MauveTonal.x90,
                onContainer = MauveTonal.x10,
            ),
        ),
    )

    override fun medium(): ContrastedColorFamilies {
        return ContrastedColorFamilies(
            primary = ColorFamily(
                color = BlackTonal.x1,
                onColor = WhiteTonal.x6,
                container = WhiteTonal.x6,
                onContainer = BlackTonal.x1,
            ),
            secondary = ColorFamily(
                color = UranianBlueTonal.x40,
                onColor = UranianBlueTonal.x99,
                container = UranianBlueTonal.x90,
                onContainer = UranianBlueTonal.x10,
            ),
            tertiary = ColorFamily(
                color = UranianBlueTonal.x40,
                onColor = UranianBlueTonal.x99,
                container = UranianBlueTonal.x90,
                onContainer = UranianBlueTonal.x10,
            ),
            error = ColorFamily(
                color = JasperTonal.x40,
                onColor = JasperTonal.x99,
                container = JasperTonal.x90,
                onContainer = JasperTonal.x10,
            ),
            extensions = TwoCentsColorExtension(
                incomes = ColorFamily(
                    color = NyanzaTonal.x40,
                    onColor = NyanzaTonal.x99,
                    container = NyanzaTonal.x90,
                    onContainer = NyanzaTonal.x10,
                ),
                fixedExpenses = ColorFamily(
                    color = AmaranthPinkTonal.x40,
                    onColor = AmaranthPinkTonal.x99,
                    container = AmaranthPinkTonal.x90,
                    onContainer = AmaranthPinkTonal.x10,
                ),
                variableExpenses = ColorFamily(
                    color = VanillaTonal.x40,
                    onColor = VanillaTonal.x99,
                    container = VanillaTonal.x90,
                    onContainer = VanillaTonal.x10,
                ),
                seasonalExpenses = ColorFamily(
                    color = MauveTonal.x40,
                    onColor = MauveTonal.x99,
                    container = MauveTonal.x90,
                    onContainer = MauveTonal.x10,
                ),
            ),
        )
    }

    override fun high(): ContrastedColorFamilies {
        return ContrastedColorFamilies(
            primary = ColorFamily(
                color = BlackTonal.x1,
                onColor = WhiteTonal.x6,
                container = WhiteTonal.x6,
                onContainer = BlackTonal.x1,
            ),
            secondary = ColorFamily(
                color = UranianBlueTonal.x30,
                onColor = UranianBlueTonal.x99,
                container = UranianBlueTonal.x90,
                onContainer = UranianBlueTonal.x10,
            ),
            tertiary = ColorFamily(
                color = UranianBlueTonal.x30,
                onColor = UranianBlueTonal.x99,
                container = UranianBlueTonal.x90,
                onContainer = UranianBlueTonal.x10,
            ),
            error = ColorFamily(
                color = JasperTonal.x30,
                onColor = JasperTonal.x99,
                container = JasperTonal.x90,
                onContainer = JasperTonal.x10,
            ),
            extensions = TwoCentsColorExtension(
                incomes = ColorFamily(
                    color = NyanzaTonal.x30,
                    onColor = NyanzaTonal.x99,
                    container = NyanzaTonal.x90,
                    onContainer = NyanzaTonal.x10,
                ),
                fixedExpenses = ColorFamily(
                    color = AmaranthPinkTonal.x30,
                    onColor = AmaranthPinkTonal.x99,
                    container = AmaranthPinkTonal.x90,
                    onContainer = AmaranthPinkTonal.x10,
                ),
                variableExpenses = ColorFamily(
                    color = VanillaTonal.x30,
                    onColor = VanillaTonal.x99,
                    container = VanillaTonal.x90,
                    onContainer = VanillaTonal.x10,
                ),
                seasonalExpenses = ColorFamily(
                    color = MauveTonal.x40,
                    onColor = MauveTonal.x99,
                    container = MauveTonal.x90,
                    onContainer = MauveTonal.x10,
                ),
            ),
        )
    }
}
