package br.com.mob1st.core.design.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import br.com.mob1st.core.design.atoms.properties.dimensions.width
import br.com.mob1st.core.design.atoms.properties.texts.rememberAnnotatedString
import br.com.mob1st.core.design.atoms.spacing.Spacings
import br.com.mob1st.core.design.atoms.theme.LocalPane
import br.com.mob1st.core.design.molecules.buttons.ButtonState
import br.com.mob1st.core.design.molecules.buttons.PrimaryButton

@Composable
fun ButtonsBar(
    primary: ButtonState,
    secondary: ButtonState?,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: (() -> Unit)?,
) {
    val pane = LocalPane.current
    val buttonsWidth =
        remember {
            pane.columns(2)
        }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(vertical = Spacings.x8),
    ) {
        Box(modifier = Modifier.width(buttonsWidth)) {
            Spacer(modifier = Modifier.fillMaxWidth())
            this@Row.AnimatedVisibility(visible = secondary != null) {
                requireNotNull(secondary)
                requireNotNull(onSecondaryClick)
                val secondaryText = rememberAnnotatedString(text = secondary.text)
                TextButton(
                    onClick = onSecondaryClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = secondaryText)
                }
            }
        }
        PrimaryButton(
            modifier = Modifier.width(buttonsWidth),
            buttonState = primary,
            onClick = onPrimaryClick,
        )
    }
}
