package br.com.mob1st.core.design.molecules.buttons

import br.com.mob1st.core.design.atoms.properties.texts.Text

data class ButtonState(
    val text: Text,
    val isLoading: Boolean = false,
    val isEnabled: Boolean = true,
)
