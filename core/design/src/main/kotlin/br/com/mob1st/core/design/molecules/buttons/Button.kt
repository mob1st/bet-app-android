package br.com.mob1st.core.design.molecules.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.theme.LocalPane
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun Button(
    style: ButtonStyle = LocalButtonStyle.current,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    when (style) {
        ButtonStyle.Primary -> PrimaryButton(
            enabled = enabled,
            onClick = onClick,
            content = content,
        )

        ButtonStyle.Secondary -> SecondaryButton(
            enabled = enabled,
            onClick = onClick,
            content = content,
        )

        ButtonStyle.Textual -> TextButton(
            enabled = enabled,
            onClick = onClick,
            content = content,
        )
    }
}

@Composable
fun PrimaryButton(
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    androidx.compose.material3.Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        onClick = onClick,
        content = content,
    )
}

@Composable
fun SecondaryButton(
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        content = content,
        onClick = onClick,
    )
}

@Composable
fun TextButton(
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    androidx.compose.material3.TextButton(
        enabled = enabled,
        content = content,
        onClick = onClick,
    )
}

object ButtonDefaults {
    /**
     * The default width for the button.
     * It excludes the [TextButton] style.
     */
    val width @Composable get() = LocalPane.current.columns(4)
}

/**
 * Provides the current [ButtonStyle] applied in the composition tree.
 */
val LocalButtonStyle = compositionLocalOf {
    ButtonStyle.Primary
}

/**
 * The style to be used in the [Button] function.
 */
enum class ButtonStyle {
    /**
     * The primary button style should be used for the most important action.
     */
    Primary,

    /**
     * The secondary button style should be used for the less important action.
     */
    Secondary,

    /**
     * The tertiary button style should be used for the least important action.
     */
    Textual,
}

@Composable
@ThemedPreview
private fun ButtonsPreview() {
    TwoCentsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryButton(onClick = { /*TODO*/ }) {
                Text(text = "Primary Button")
            }
            SecondaryButton(onClick = { /*TODO*/ }) {
                Text(text = "Secondary Button")
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Text Button")
            }
        }
    }
}
