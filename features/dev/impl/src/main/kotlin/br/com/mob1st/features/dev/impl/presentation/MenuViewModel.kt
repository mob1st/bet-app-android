package br.com.mob1st.features.dev.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.state.contracts.ListSelectionManager
import br.com.mob1st.core.state.contracts.NavigationConsumeManager
import br.com.mob1st.core.state.contracts.PrimaryCtaManager
import br.com.mob1st.core.state.contracts.SnackbarManager
import br.com.mob1st.core.state.contracts.StateOutputManager
import br.com.mob1st.features.dev.impl.domain.GetMenuUseCase
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.DefaultSnackManager
import br.com.mob1st.features.utils.errors.LogExceptionHandler
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
    private val snackManager: DefaultSnackManager,
) : ViewModel(),
    StateOutputManager<MenuPageState>,
    ListSelectionManager,
    NavigationConsumeManager,
    PrimaryCtaManager,
    SnackbarManager by snackManager {

    // inputs

    private val retries = MutableSharedFlow<Unit>()

    // outputs
    private val selectedItemState = MutableStateFlow<Int?>(null)
    private val commonErrorsState = MutableStateFlow<CommonError?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val output: StateFlow<MenuPageState> = combine(
        retries.onStart { emit(Unit) }.flatMapLatest { getMenuUseCase() },
        commonErrorsState,
        snackManager.peeks,
        selectedItemState,
        ::MenuPageState
    ).stateInRetained(
        scope = viewModelScope + LogExceptionHandler(::handleError),
        initialValue = MenuPageState()
    )

    private fun handleError(throwable: Throwable) {
        commonErrorsState.update {
            CommonError.from(throwable)
        }
    }

    override fun selectItem(position: Int) {
        if (position == MenuPageState.GALLERY_POSITION) {
            selectedItemState.update { position }
        } else {
            snackManager.offer(MenuPageState.todoSnack)
        }
    }

    override fun consumeNavigation() {
        selectedItemState.update { null }
    }

    override fun primaryAction() {
        viewModelScope.launch {
            retries.emit(Unit)
        }
    }
}
