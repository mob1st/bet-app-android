package br.com.mob1st.core.design.molecules.keyboard

import android.icu.text.DecimalFormatSymbols
import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.R
import br.com.mob1st.core.design.atoms.colors.material.LocalExtensionsColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.ColorCombination
import br.com.mob1st.core.design.atoms.icons.Calendar
import br.com.mob1st.core.design.atoms.icons.CheckIcon
import br.com.mob1st.core.design.atoms.icons.Delete
import br.com.mob1st.core.design.atoms.icons.Undo
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.utils.LocalLocale
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview
import kotlin.math.E

/**
 * The keyboard for currency input used in the app.
 * @param onClickKey The callback to be called when a key is clicked.
 * @see Key to check the available keys.
 */
@Composable
fun Keyboard(
    onClickKey: (key: Key) -> Unit,
) {
    BoxWithConstraints {
        val height = remember(maxWidth) {
            maxWidth - (Spacings.x1 * 2) - (KeyboardDefaults.spacing * 3)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Spacings.x1),
            horizontalArrangement = Arrangement.spacedBy(KeyboardDefaults.spacing),
        ) {
            KeyboardColumn {
                NumericButton(NumericKey.Seven, onClickItem = onClickKey)
                NumericButton(NumericKey.Four, onClickItem = onClickKey)
                NumericButton(NumericKey.One, onClickItem = onClickKey)
                UndoButton(onClickItem = onClickKey)
            }
            KeyboardColumn {
                NumericButton(NumericKey.Eight, onClickItem = onClickKey)
                NumericButton(NumericKey.Five, onClickItem = onClickKey)
                NumericButton(NumericKey.Two, onClickItem = onClickKey)
                NumericButton(NumericKey.Zero, onClickItem = onClickKey)
            }
            KeyboardColumn {
                NumericButton(NumericKey.Nine, onClickItem = onClickKey)
                NumericButton(NumericKey.Six, onClickItem = onClickKey)
                NumericButton(NumericKey.Three, onClickItem = onClickKey)
                DecimalButton(onClickItem = onClickKey)
            }
            KeyboardColumn {
                DeleteButton(onClickItem = onClickKey)
                CalendarButton(onClickItem = onClickKey)
                DoneButton(onClickItem = onClickKey)
            }
        }
    }
}

@Composable
private fun RowScope.KeyboardColumn(
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(KeyboardDefaults.spacing),
        content = content,
    )
}

@Composable
private fun ColumnScope.NumericButton(
    number: NumericKey,
    onClickItem: (key: Key) -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        onClick = { onClickItem(number) },
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Text(
            text = number.number.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.5,
            ),
        )
    }
}

@Composable
private fun ColumnScope.CalendarButton(onClickItem: (key: Key) -> Unit) {
    BaseButton(
        combination = ColorCombination(
            background = MaterialTheme.colorScheme.secondaryContainer,
            content = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        onClick = { onClickItem(FunctionKey.Calendar) },
    ) {
        Calendar()
    }
}

@Composable
private fun ColumnScope.UndoButton(onClickItem: (key: Key) -> Unit) {
    BaseButton(
        combination = LocalExtensionsColorFamilies.current.variableExpenses.containerCombination,
        onClick = { onClickItem(FunctionKey.Undo) },
    ) {
        Undo(contentDescription = stringResource(id = R.string.core_design_next_button_undo))
    }
}

@Composable
private fun ColumnScope.DecimalButton(onClickItem: (key: Key) -> Unit) {
    BaseButton(
        combination = ColorCombination(
            background = MaterialTheme.colorScheme.surfaceContainerHigh,
            content = MaterialTheme.colorScheme.onSurface,
        ),
        onClick = { onClickItem(FunctionKey.Decimal) },
    ) {
        val decimalStyle = LocalTextStyle.current.copy(
            fontSize = LocalTextStyle.current.fontSize * E,
        )
        val locale = LocalLocale.current
        val separator = remember(locale) {
            DecimalFormatSymbols.getInstance(locale).decimalSeparator.toString()
        }
        Text(
            text = separator,
            style = decimalStyle,
            modifier = Modifier.paddingFromBaseline(bottom = 100.dp),
        )
    }
}

@Composable
private fun ColumnScope.DeleteButton(onClickItem: (key: Key) -> Unit) {
    BaseButton(
        onClick = { onClickItem(FunctionKey.Delete) },
        combination = LocalExtensionsColorFamilies.current.fixedExpenses.containerCombination,
    ) {
        Delete(contentDescription = stringResource(id = R.string.core_design_next_button_delete))
    }
}

@Composable
private fun ColumnScope.DoneButton(
    onClickItem: (key: Key) -> Unit,
) {
    BaseButton(
        combination = ColorCombination(
            background = MaterialTheme.colorScheme.primary,
            content = MaterialTheme.colorScheme.onPrimary,
        ),
        weight = 2f,
        onClick = { onClickItem(FunctionKey.Done) },
    ) {
        CheckIcon(contentDescription = stringResource(id = R.string.core_design_next_button_complete))
    }
}

@Composable
private fun ColumnScope.BaseButton(
    combination: ColorCombination,
    @FloatRange(from = 0.0, fromInclusive = false)
    weight: Float = 1f,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .weight(weight),
        colors = ButtonDefaults.buttonColors(
            containerColor = combination.background,
            contentColor = combination.content,
        ),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.bodyLarge) {
            content()
        }
    }
}

private object KeyboardDefaults {
    val spacing = 2.dp
}

@ThemedPreview
@Composable
private fun KeyboardPreview() {
    PreviewTheme {
        Keyboard(onClickKey = {})
    }
}
