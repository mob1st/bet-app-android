package br.com.mob1st.features.dev.impl.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.state.contracts.NavigationDelegate
import br.com.mob1st.core.state.contracts.NavigationManager
import br.com.mob1st.core.state.contracts.UiStateOutputManager
import br.com.mob1st.core.state.managers.DialogDelegate
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.SnackbarDelegate
import br.com.mob1st.core.state.managers.SnackbarManager
import br.com.mob1st.core.state.managers.mapCatching
import br.com.mob1st.features.dev.impl.domain.GetDevMenuUseCase
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.dialogErrorHandler
import kotlinx.coroutines.flow.StateFlow

internal class DevMenuViewModel(
    private val getMenuUseCase: GetDevMenuUseCase,
    private val holder: DevMenuUiStateHolder,
) : ViewModel(),
    UiStateOutputManager<DevMenuUiState>,
    NavigationManager<DevSettingsNavTarget> by NavigationDelegate(),
    SnackbarManager<DevMenuSnackbar> by SnackbarDelegate(),
    DialogManager<CommonError> by DialogDelegate() {
    override val uiStateOutput: StateFlow<DevMenuUiState> = getDevMenu()
        .stateInRetained(
            scope = viewModelScope,
            initialValue = DevMenuUiState.Empty,
        )

    private fun getDevMenu() =
        getMenuUseCase().mapCatching(
            map = holder::asUiState,
            errorHandler = dialogErrorHandler,
        )

    fun selectItem(position: Int) {
        if (holder.isImplemented(position)) {
            goTo(holder.getNavTarget(position))
        } else {
            showSnackbar(DevMenuSnackbar.Todo)
        }
    }
}
