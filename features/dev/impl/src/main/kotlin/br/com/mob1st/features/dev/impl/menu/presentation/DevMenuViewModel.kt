package br.com.mob1st.features.dev.impl.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.coroutines.asResultFlow
import br.com.mob1st.core.kotlinx.coroutines.trigger
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.core.state.contracts.ListSelectionManager
import br.com.mob1st.core.state.contracts.SnackbarDismissManager
import br.com.mob1st.core.state.contracts.StateOutputManager
import br.com.mob1st.features.dev.impl.menu.domain.GetDevMenuUseCase
import br.com.mob1st.features.utils.errors.QueueSnackDismissManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class DevMenuViewModel(
    getMenuUseCase: GetDevMenuUseCase,
    private val snackManager: QueueSnackDismissManager,
) : ViewModel(),
    StateOutputManager<MenuPageState>,
    ListSelectionManager,
    SnackbarDismissManager by snackManager {

    // inputs
    private val retryInput = MutableSharedFlow<Unit>()

    // outputs
    private val selectedItemOutput = MutableStateFlow<Int?>(null)

    override val output: StateFlow<MenuPageState> = combine(
        retryInput.trigger { getMenuUseCase().asResultFlow() },
        snackManager.output(viewModelScope),
        selectedItemOutput,
        MenuPageState::transform
    ).stateInRetained(
        scope = viewModelScope,
        initialValue = MenuPageState.Empty
    )

    override fun selectItem(position: Int) {
        val value = checkIs<MenuPageState.Loaded>(output.value)
        if (value.menu.isAllowed(position)) {
            selectedItemOutput.update { position }
        } else {
            snackManager.offer(MenuPageState.TodoSnack())
        }
    }

    override fun consumeNavigation() {
        selectedItemOutput.update { null }
    }

    override fun restoreFromFailure() {
        viewModelScope.launch {
            retryInput.emit(Unit)
        }
    }
}
