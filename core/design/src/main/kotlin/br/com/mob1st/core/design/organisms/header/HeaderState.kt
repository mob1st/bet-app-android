package br.com.mob1st.core.design.organisms.header

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.design.atoms.properties.texts.rememberAnnotatedString

@Immutable
data class HeaderState(
    val text: TextState,
    val description: TextState? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(state: HeaderState) {
    TopAppBar(
        title = {
            Text(text = rememberAnnotatedString(text = state.text))
        },
    )
}
