package br.com.mob1st.core.design.atoms.colors.material.families

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

/**
 * Simplified material color scheme that groups the customizable colors of the material theme into their families
 */
data class MaterialColorFamilies(
    val primary: ColorFamily,
    val secondary: ColorFamily,
    val tertiary: ColorFamily,
    val error: ColorFamily,
    val surface: SurfaceFamily,
    val elevationFamily: ElevationFamily,
    val inverseFamily: InverseFamily,
    val backgroundFamily: BackgroundFamily,
)

/**
 * Converts the [MaterialColorFamilies] to a [ColorScheme] for light themes.
 * When updating the tokens here ensure that the [MaterialColorFamilies.toDarkColorScheme] is also updated
 */
internal fun MaterialColorFamilies.toLightColorScheme(): ColorScheme {
    return lightColorScheme(
        primary = primary.color,
        onPrimary = primary.onColor,
        primaryContainer = primary.onContainer,
        onPrimaryContainer = primary.container,
        secondary = secondary.color,
        onSecondary = secondary.onColor,
        secondaryContainer = secondary.onContainer,
        onSecondaryContainer = secondary.container,
        tertiary = tertiary.color,
        onTertiary = tertiary.onColor,
        tertiaryContainer = tertiary.onContainer,
        onTertiaryContainer = tertiary.container,
        error = error.color,
        onError = error.onColor,
        errorContainer = error.container,
        onErrorContainer = error.onContainer,
        surfaceDim = surface.dim,
        surface = surface.surface,
        surfaceBright = surface.bright,
        surfaceContainerLowest = surface.containerLowest,
        surfaceContainerLow = surface.containerLow,
        surfaceContainer = surface.container,
        surfaceContainerHigh = surface.containerHigh,
        surfaceContainerHighest = surface.containerHighest,
        onSurface = surface.onSurface,
        onSurfaceVariant = surface.onSurfaceVariant,
        outline = surface.outline,
        outlineVariant = surface.outlineVariant,
        inverseSurface = inverseFamily.inverseSurface,
        inverseOnSurface = inverseFamily.inverseOnSurface,
        inversePrimary = inverseFamily.inversePrimary,
        scrim = elevationFamily.scrim,
        background = backgroundFamily.background,
        onBackground = backgroundFamily.onBackground,
    )
}

/**
 * Converts the [MaterialColorFamilies] to a [ColorScheme] for dark themes
 * When updating the tokens here ensure that the [MaterialColorFamilies.toLightColorScheme] is also updated
 */
internal fun MaterialColorFamilies.toDarkColorScheme(): ColorScheme {
    return darkColorScheme(
        primary = primary.color,
        onPrimary = primary.onColor,
        primaryContainer = primary.onContainer,
        onPrimaryContainer = primary.container,
        secondary = secondary.color,
        onSecondary = secondary.onColor,
        secondaryContainer = secondary.onContainer,
        onSecondaryContainer = secondary.container,
        tertiary = tertiary.color,
        onTertiary = tertiary.onColor,
        tertiaryContainer = tertiary.onContainer,
        onTertiaryContainer = tertiary.container,
        error = error.color,
        onError = error.onColor,
        errorContainer = error.container,
        onErrorContainer = error.onContainer,
        surfaceDim = surface.dim,
        surface = surface.surface,
        surfaceBright = surface.bright,
        surfaceContainerLowest = surface.containerLowest,
        surfaceContainerLow = surface.containerLow,
        surfaceContainer = surface.container,
        surfaceContainerHigh = surface.containerHigh,
        surfaceContainerHighest = surface.containerHighest,
        onSurface = surface.onSurface,
        onSurfaceVariant = surface.onSurfaceVariant,
        outline = surface.outline,
        outlineVariant = surface.outlineVariant,
        inverseSurface = inverseFamily.inverseSurface,
        inverseOnSurface = inverseFamily.inverseOnSurface,
        inversePrimary = inverseFamily.inversePrimary,
        scrim = elevationFamily.scrim,
        background = backgroundFamily.background,
        onBackground = backgroundFamily.onBackground,
    )
}
