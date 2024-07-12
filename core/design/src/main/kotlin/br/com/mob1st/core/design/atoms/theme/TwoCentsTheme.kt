package br.com.mob1st.core.design.atoms.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import br.com.mob1st.core.design.atoms.colors.contrast.LocalUiContrast
import br.com.mob1st.core.design.atoms.colors.contrast.UiContrast
import br.com.mob1st.core.design.atoms.colors.contrast.UiContrastEffect
import br.com.mob1st.core.design.atoms.colors.contrast.getCurrentUiContrast
import br.com.mob1st.core.design.atoms.colors.material.LocalFixedExpensesFamily
import br.com.mob1st.core.design.atoms.colors.material.LocalIncomesFamily
import br.com.mob1st.core.design.atoms.colors.material.LocalSeasonalExpensesFamily
import br.com.mob1st.core.design.atoms.colors.material.LocalVariableExpensesFamily
import br.com.mob1st.core.design.atoms.colors.material.TwoCentsColorScheme
import br.com.mob1st.core.design.atoms.shapes.ShapesFactory
import br.com.mob1st.core.design.atoms.typography.TypographyFactory

/**
 * Base theme style for the app.
 * The role of this theme is setup the [MaterialTheme] parameters.
 * This function does not start LocalProviders or does any setup for the app, which make it useful for @Preview
 * functions.
 * @param content The content to be displayed inside the theme.
 */
@Composable
fun TwoCentsTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    var uiContrastLevel by remember {
        mutableFloatStateOf(value = context.getCurrentUiContrast())
    }
    val uiContrast by remember {
        derivedStateOf { UiContrast.from(uiContrastLevel) }
    }

    CompositionLocalProvider(LocalUiContrast provides uiContrast) {
        MaterialThemeWrapper(content)
    }

    UiContrastEffect {
        uiContrastLevel = it
    }
}

@Composable
private fun MaterialThemeWrapper(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val uiContrast = LocalUiContrast.current
    val (materialScheme, twoCentsExtensions) = remember(isDark, uiContrast) {
        TwoCentsColorScheme.create(isDark, uiContrast)
    }
    val typography = remember {
        TypographyFactory.create()
    }
    val shapes = remember {
        ShapesFactory.create()
    }

    CompositionLocalProvider(
        LocalIncomesFamily provides twoCentsExtensions.incomes,
        LocalFixedExpensesFamily provides twoCentsExtensions.fixedExpenses,
        LocalVariableExpensesFamily provides twoCentsExtensions.variableExpenses,
        LocalSeasonalExpensesFamily provides twoCentsExtensions.seasonalExpenses,
    ) {
        MaterialTheme(
            colorScheme = materialScheme,
            typography = typography,
            shapes = shapes,
        ) {
            content()
        }
    }
}
