package br.com.mob1st.core.design.templates

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.organisms.ModalState
import br.com.mob1st.core.design.organisms.snack.SnackbarState

@Immutable
interface UiState {
    val snackbarState: SnackbarState?
    val modal: ModalState?
}
