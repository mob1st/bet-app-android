package br.com.mob1st.features.dev.impl.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.state.contracts.ListSelectionManager
import br.com.mob1st.core.state.contracts.NavigationConsumeManager
import br.com.mob1st.core.state.contracts.PrimaryCtaManager
import br.com.mob1st.core.state.contracts.SnackbarManager
import br.com.mob1st.core.state.contracts.StateOutputManager
import br.com.mob1st.features.dev.impl.menu.domain.GetMenuUseCase
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.LogExceptionHandler
import br.com.mob1st.features.utils.errors.QueueSnackManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class MenuViewModel(
    getMenuUseCase: GetMenuUseCase,
    private val snackManager: QueueSnackManager,
) : ViewModel(snackManager),
    StateOutputManager<MenuPageState>,
    ListSelectionManager,
    NavigationConsumeManager,
    PrimaryCtaManager,
    SnackbarManager by snackManager {

    // inputs
    private val retryInput = MutableSharedFlow<Unit>()

    // outputs
    private val selectedItemOutput = MutableStateFlow<Int?>(null)
    private val commonErrorOutput = MutableStateFlow<CommonError?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val output: StateFlow<MenuPageState> = combine(
        retryInput.onStart { emit(Unit) }.flatMapLatest { getMenuUseCase() },
        commonErrorOutput,
        snackManager.peeks,
        selectedItemOutput,
        ::MenuPageState
    ).stateInRetained(
        scope = viewModelScope + LogExceptionHandler(::handleError),
        initialValue = MenuPageState()
    )

    private fun handleError(throwable: Throwable) {
        commonErrorOutput.update {
            CommonError.from(throwable)
        }
    }

    override fun selectItem(position: Int) {
        if (position == MenuPageState.GALLERY_POSITION) {
            selectedItemOutput.update { position }
        } else {
            snackManager.offer(MenuPageState.todoSnack)
        }
    }

    override fun consumeNavigation() {
        selectedItemOutput.update { null }
    }

    override fun primaryAction() {
        viewModelScope.launch {
            retryInput.emit(Unit)
        }
    }
}
