package br.com.mob1st.core.design.organisms.lists

import br.com.mob1st.core.design.organisms.snack.SnackQueue
import kotlinx.coroutines.flow.MutableStateFlow

class SnackbarStateManager {

    private val snackbarStateFlow = MutableStateFlow(SnackQueue())
}
