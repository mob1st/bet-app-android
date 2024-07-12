package br.com.mob1st.core.design.atoms.colors.material

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import br.com.mob1st.core.design.atoms.colors.material.families.ColorFamily

val LocalIncomesFamily = compositionLocalOf<ColorFamily> {
    error("No IncomesFamily provided")
}

val LocalFixedExpensesFamily = compositionLocalOf<ColorFamily> {
    error("No FixedExpensesFamily provided")
}

val LocalVariableExpensesFamily = compositionLocalOf<ColorFamily> {
    error("No VariableExpensesFamily provided")
}

val LocalSeasonalExpensesFamily = compositionLocalOf<ColorFamily> {
    error("No SeasonalExpensesFamily provided")
}

val ColorScheme.incomesFamily: ColorFamily
    @Composable get() = LocalIncomesFamily.current

val ColorScheme.fixedExpensesFamily: ColorFamily
    @Composable get() = LocalFixedExpensesFamily.current

val ColorScheme.variableExpensesFamily: ColorFamily
    @Composable get() = LocalVariableExpensesFamily.current

val ColorScheme.seasonalExpensesFamily: ColorFamily
    @Composable get() = LocalSeasonalExpensesFamily.current
