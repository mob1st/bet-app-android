package br.com.mob1st.core.design.molecules.buttons

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import br.com.mob1st.core.design.atoms.icons.BackIcon
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.utils.ThemedPreview

@Composable
fun TopBackButton(
    onBackClicked: () -> Unit,
) {
    IconButton(
        onClick = onBackClicked,
    ) {
        BackIcon()
    }
}

@Composable
@ThemedPreview
private fun TopBackButtonPreview() {
    BetTheme {
        TopBackButton(onBackClicked = { })
    }
}
