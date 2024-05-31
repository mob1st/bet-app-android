package br.com.mob1st.features.dev.impl.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.coroutines.mapCatching
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.core.state.contracts.NavigationDelegate
import br.com.mob1st.core.state.contracts.NavigationManager
import br.com.mob1st.core.state.contracts.UiStateManager
import br.com.mob1st.core.state.managers.launchIn
import br.com.mob1st.features.dev.impl.domain.GetDevMenuUseCase
import br.com.mob1st.features.utils.uimessages.ModalStateManager
import br.com.mob1st.features.utils.uimessages.SnackbarStateManager
import br.com.mob1st.features.utils.uimessages.UiMessageDelegate
import kotlinx.coroutines.flow.StateFlow

internal class DevMenuViewModel(
    getMenuUseCase: GetDevMenuUseCase,
    private val navigationDelegate: NavigationDelegate<DevMenuNavigable> = NavigationDelegate(),
    private val uiMessageDelegate: UiMessageDelegate = UiMessageDelegate(),
) : ViewModel(),
    UiStateManager<DevMenuUiState>,
    NavigationManager<DevMenuNavigable> by navigationDelegate,
    SnackbarStateManager by uiMessageDelegate,
    ModalStateManager by uiMessageDelegate {
    override val uiOutput: StateFlow<DevMenuUiState> =
        getMenuUseCase()
            .mapCatching(
                map = DevMenuUiState::Loaded,
                catch = uiMessageDelegate::catchAsModal,
            )
            .stateInRetained(
                scope = viewModelScope,
                initialValue = DevMenuUiState.Empty,
            )

    fun selectItem(position: Int): Unit =
        launchIn(uiMessageDelegate.catchAsModal()) {
            val value = checkIs<DevMenuUiState.Loaded>(uiOutput.value)
            if (value.menu.entries[position].isImplemented) {
                navigationDelegate.goTo(DevMenuNavigable(value.menu.entries[position]))
            } else {
                uiMessageDelegate.showSnackbar(DevMenuUiState.TodoSnack())
            }
        }
}
