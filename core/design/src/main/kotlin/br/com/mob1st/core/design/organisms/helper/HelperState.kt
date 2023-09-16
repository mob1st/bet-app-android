package br.com.mob1st.core.design.organisms.helper

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.Text
import br.com.mob1st.core.design.molecules.buttons.ButtonState

@Immutable
data class HelperState(
    val title: Text,
    val subtitle: Text,
    val buttonState: ButtonState,
)
