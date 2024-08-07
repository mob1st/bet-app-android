package br.com.mob1st.core.design.molecules.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.icons.CheckIcon
import br.com.mob1st.core.design.molecules.loading.CrossfadeLoading
import br.com.mob1st.core.design.utils.PreviewTheme
import br.com.mob1st.core.design.utils.ThemedPreview

/**
 * A button for the most prominent actions in a screen.
 * It's a handy function to use the [ExtendedFloatingActionButton] with a predefined style.
 * It supports expanded and loading states.
 * In case [isLoading] is true, the button will show a loading indicator instead of the icon.
 * In case [isExpanded] is false, the button will hide the text and let only the icon be shown.
 * @param modifier the modifier for the button.
 * @param isLoading if the button is in a loading state.
 * @param isExpanded if the button should be expanded.
 * @param onClick the action to be executed when the button is clicked.
 * @param icon the icon to be shown in the button.
 * @param text the text to be shown in the button.
 * @see [ExtendedFloatingActionButton]
 */
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isExpanded: Boolean = true,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier.wrapContentSize(),
        expanded = isExpanded,
        icon = {
            IconSlot(isLoading = isLoading, icon = icon)
        },
        text = text,
        onClick = onClick,
    )
}

@Composable
private fun IconSlot(
    isLoading: Boolean,
    icon: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.size(ButtonDefaults.iconSize),
        contentAlignment = Alignment.Center,
    ) {
        CrossfadeLoading(
            isLoading = isLoading,
        ) {
            icon()
        }
    }
}

/**
 * The style to be used in the [Button] function.
 */
object ButtonDefaults {
    /**
     * The default size for the icons in the buttons.
     */
    val iconSize = 18.dp
}

@Composable
@ThemedPreview
private fun ButtonsPreview() {
    PreviewTheme {
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
            PrimaryButton(
                onClick = { },
                icon = {
                    CheckIcon(contentDescription = null)
                },
                text = { Text(text = "Confirm") },
            )
        }
    }
}
