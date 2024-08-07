package br.com.mob1st.core.design.organisms.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.icons.BackIcon
import br.com.mob1st.core.design.atoms.theme.TwoCentsTheme
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun TopBackButton(
    onBackClicked: () -> Unit,
) {
    IconButton(
        onClick = onBackClicked,
    ) {
        BackIcon(modifier = Modifier.size(24.dp))
    }
}

@Composable
@ThemedPreview
private fun TopBackButtonPreview() {
    TwoCentsTheme {
        TopBackButton(onBackClicked = { })
    }
}
