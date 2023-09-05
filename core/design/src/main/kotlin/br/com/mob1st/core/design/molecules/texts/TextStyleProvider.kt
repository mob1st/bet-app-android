package br.com.mob1st.core.design.molecules.texts

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
internal sealed interface TextStyleProvider {
    fun of(typography: Typography, textSize: TextSize): TextStyle
}

internal data object DisplayStyleProvider : TextStyleProvider {
    override fun of(typography: Typography, textSize: TextSize): TextStyle {
        return when (textSize) {
            TextSize.Small -> typography.displaySmall
            TextSize.Medium -> typography.displayMedium
            TextSize.Large -> typography.displayLarge
        }
    }
}

internal data object HeadlineStyleProvider : TextStyleProvider {
    override fun of(typography: Typography, textSize: TextSize): TextStyle {
        return when (textSize) {
            TextSize.Small -> typography.headlineSmall
            TextSize.Medium -> typography.headlineMedium
            TextSize.Large -> typography.headlineLarge
        }
    }
}

internal data object TitleStyleProvider : TextStyleProvider {
    override fun of(typography: Typography, textSize: TextSize): TextStyle {
        return when (textSize) {
            TextSize.Small -> typography.titleSmall
            TextSize.Medium -> typography.titleMedium
            TextSize.Large -> typography.titleLarge
        }
    }
}

internal data object BodyStyleProvider : TextStyleProvider {
    override fun of(typography: Typography, textSize: TextSize): TextStyle {
        return when (textSize) {
            TextSize.Small -> typography.bodySmall
            TextSize.Medium -> typography.bodyMedium
            TextSize.Large -> typography.bodyLarge
        }
    }
}

internal data object LabelStyleProvider : TextStyleProvider {
    override fun of(typography: Typography, textSize: TextSize): TextStyle {
        return when (textSize) {
            TextSize.Small -> typography.labelSmall
            TextSize.Medium -> typography.labelMedium
            TextSize.Large -> typography.labelLarge
        }
    }
}

enum class TextSize {
    Small,
    Medium,
    Large,
}
