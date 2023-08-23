package br.com.mob1st.core.design.atoms.colors

import androidx.compose.material3.ColorScheme

/**
 * Factory to create a [ColorScheme] based on the current device theme.
 */
internal object AppSchemeFactory {

    /**
     * Creates a [ColorScheme] based on the current device theme.
     */
    fun create(isDark: Boolean): ColorScheme {
        return AppColorPalette.init().run {
            if (isDark) {
                dark()
            } else {
                light()
            }
        }
    }
}

private fun AppColorPalette.dark(): ColorScheme {
    return ColorScheme(
        primary = primary.x80,
        onPrimary = primary.x20,
        primaryContainer = primary.x30,
        onPrimaryContainer = primary.x90,

        secondary = secondary.x80,
        onSecondary = secondary.x20,
        secondaryContainer = secondary.x30,
        onSecondaryContainer = secondary.x90,

        tertiary = tertiary.x80,
        onTertiary = tertiary.x20,
        tertiaryContainer = tertiary.x30,
        onTertiaryContainer = tertiary.x90,

        error = error.x80,
        onError = error.x20,
        errorContainer = error.x30,
        onErrorContainer = error.x90,

        background = neutral.x10,
        onBackground = neutral.x90,
        surface = neutral.x10,
        onSurface = neutral.x90,

        surfaceVariant = neutralVariant.x30,
        onSurfaceVariant = neutralVariant.x80,
        surfaceTint = primary.x80,

        outline = neutralVariant.x60,

        outlineVariant = neutralVariant.x30,

        inversePrimary = primary.x40,
        inverseSurface = neutral.x90,
        inverseOnSurface = neutral.x10,

        scrim = neutral.x0
    )
}

private fun AppColorPalette.light(): ColorScheme {
    return ColorScheme(
        primary = primary.x40,
        onPrimary = primary.x100,
        primaryContainer = primary.x90,
        onPrimaryContainer = primary.x10,

        secondary = secondary.x40,
        onSecondary = secondary.x100,
        secondaryContainer = secondary.x90,
        onSecondaryContainer = secondary.x10,

        tertiary = tertiary.x40,
        onTertiary = tertiary.x100,
        tertiaryContainer = tertiary.x90,
        onTertiaryContainer = tertiary.x10,

        error = error.x40,
        onError = error.x100,
        errorContainer = error.x90,
        onErrorContainer = error.x10,

        background = neutral.x99,
        onBackground = neutral.x10,

        surface = neutral.x99,
        onSurface = neutral.x10,
        surfaceTint = primary.x40,

        outline = neutralVariant.x50,
        outlineVariant = neutralVariant.x80,
        surfaceVariant = neutralVariant.x90,
        onSurfaceVariant = neutralVariant.x30,

        inverseSurface = neutral.x20,
        inverseOnSurface = neutral.x95,
        inversePrimary = primary.x80,

        scrim = neutral.x0
    )
}
