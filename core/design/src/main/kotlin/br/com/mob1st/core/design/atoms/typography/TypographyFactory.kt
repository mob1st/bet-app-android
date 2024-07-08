package br.com.mob1st.core.design.atoms.typography

import androidx.compose.material3.Typography

/**
 * Creates a Material3 [Typography] object from the app atoms.
 */
object TypographyFactory {
    /**
     * Creates the [Typography] object.
     */
    fun create(): Typography {
        val roles = DefaultFontRoles
        return Typography(
            displayLarge = roles.display.large,
            displayMedium = roles.display.medium,
            displaySmall = roles.display.small,
            headlineLarge = roles.headline.large,
            headlineMedium = roles.headline.medium,
            headlineSmall = roles.headline.small,
            titleLarge = roles.title.large,
            titleMedium = roles.title.medium,
            titleSmall = roles.title.small,
            bodyLarge = roles.body.large,
            bodyMedium = roles.body.medium,
            bodySmall = roles.body.small,
            labelLarge = roles.label.large,
            labelMedium = roles.label.medium,
            labelSmall = roles.label.small,
        )
    }
}
