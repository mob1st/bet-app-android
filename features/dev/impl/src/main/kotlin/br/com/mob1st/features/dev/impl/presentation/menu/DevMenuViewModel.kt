package br.com.mob1st.features.dev.impl.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.coroutines.asResultFlow
import br.com.mob1st.core.kotlinx.coroutines.trigger
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.core.state.contracts.ListSelectionManager
import br.com.mob1st.core.state.contracts.NavigationManager
import br.com.mob1st.core.state.contracts.SnackbarDismissManager
import br.com.mob1st.core.state.contracts.StateOutputManager
import br.com.mob1st.features.dev.impl.domain.GetDevMenuUseCase
import br.com.mob1st.features.utils.errors.SnackDismissManagerDelegate
import br.com.mob1st.features.utils.navigation.NavigationManagerDelegate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

internal class DevMenuViewModel(
    getMenuUseCase: GetDevMenuUseCase,
    private val snackManager: SnackDismissManagerDelegate,
    private val navigationManager: NavigationManagerDelegate<Int> = NavigationManagerDelegate(),
) : ViewModel(),
    StateOutputManager<DevMenuPageState>,
    ListSelectionManager,
    NavigationManager by navigationManager,
    SnackbarDismissManager by snackManager {

    // inputs
    private val retryInput = MutableSharedFlow<Unit>()

    override val output: StateFlow<DevMenuPageState> = combine(
        retryInput.trigger { getMenuUseCase().asResultFlow() },
        snackManager.output(viewModelScope),
        navigationManager.output(viewModelScope),
        DevMenuPageState.Companion::transform
    ).stateInRetained(
        scope = viewModelScope,
        initialValue = DevMenuPageState.Empty
    )

    override fun selectItem(position: Int) {
        val value = checkIs<DevMenuPageState.Loaded>(output.value)
        if (value.menu.isAllowed(position)) {
            navigationManager.trigger(position)
        } else {
            snackManager.offer(DevMenuPageState.TodoSnack())
        }
    }

    override fun restoreFromFailure() {
        viewModelScope.launch {
            retryInput.emit(Unit)
        }
    }
}
