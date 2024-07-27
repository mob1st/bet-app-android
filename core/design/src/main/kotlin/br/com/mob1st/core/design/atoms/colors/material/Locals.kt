package br.com.mob1st.core.design.atoms.colors.material

import androidx.compose.runtime.compositionLocalOf
import br.com.mob1st.core.design.atoms.colors.material.families.MaterialColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.TwoCentsColorExtension

/**
 * Provides access to a [TwoCentsColorExtension] inside a Composable scope.
 */
val LocalExtensionsColorFamilies = compositionLocalOf<TwoCentsColorExtension> {
    error("No BackgroundColorFamily provided")
}

/**
 * Provides access to a [MaterialColorFamilies] inside a Composable scope.
 */
val LocalMaterialColorFamilies = compositionLocalOf<MaterialColorFamilies> {
    error("No BackgroundColorFamily provided")
}
