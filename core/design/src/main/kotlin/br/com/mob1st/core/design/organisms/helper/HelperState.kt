package br.com.mob1st.core.design.organisms.helper

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.core.design.molecules.buttons.ButtonState

@Immutable
data class HelperState(
    val title: LocalizedText,
    val subtitle: LocalizedText,
    val buttonState: ButtonState,
)
