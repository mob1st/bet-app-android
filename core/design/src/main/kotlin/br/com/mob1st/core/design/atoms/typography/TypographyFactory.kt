package br.com.mob1st.core.design.atoms.typography

import androidx.compose.material3.Typography

object TypographyFactory {
    fun create(): Typography {
        val display = FontCategory.Display()
        val headline = FontCategory.Headline()
        val title = FontCategory.Title()
        val body = FontCategory.Body()
        val label = FontCategory.Label()
        return Typography(
            displayLarge = display.large,
            displayMedium = display.medium,
            displaySmall = display.small,
            headlineLarge = headline.large,
            headlineMedium = headline.medium,
            headlineSmall = headline.small,
            titleLarge = title.large,
            titleMedium = title.medium,
            titleSmall = title.small,
            bodyLarge = body.large,
            bodyMedium = body.medium,
            bodySmall = body.small,
            labelLarge = label.large,
            labelMedium = label.medium,
            labelSmall = label.small,
        )
    }
}
