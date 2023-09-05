package br.com.mob1st.core.design.molecules.buttons

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.mob1st.core.design.molecules.loading.Loading

@Composable
fun PrimaryButton(buttonState: ButtonState, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = buttonState.isEnabled,
        modifier = modifier
    ) {
        Loading(isLoading = buttonState.isLoading, crossfadeLabel = "primary button loading") {
        }
    }
}
