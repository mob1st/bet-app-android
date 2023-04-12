package br.com.mob1st.features.onboarding.impl.ui

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.async.Async
import br.com.mob1st.core.state.async.launch
import br.com.mob1st.core.state.extensions.collectUpdate
import br.com.mob1st.core.state.extensions.launchEmit
import br.com.mob1st.core.state.extensions.onCollect
import br.com.mob1st.features.onboarding.impl.domain.OpenAppUseCase
import br.com.mob1st.morpheus.annotation.Consumable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

/**
 * A ViewModel that manages of the Launcher screen.
 *
 * @param openAppUseCase A use case triggered when the app opens.
 */
class LauncherViewModel(
    openAppUseCase: OpenAppUseCase,
) : ViewModel(), LauncherUiContract {

    // output state
    private val _output = MutableStateFlow(LauncherUiState())
    override val output: StateFlow<LauncherUiState> = _output.asStateFlow()

    // inputs
    private val helperPrimaryActionInput = MutableSharedFlow<Unit>()

    // actions
    private val openAppAction = Async<OpenAppUseCase.Destination> {
        flow {
            emit(openAppUseCase())
        }
    }

    init {
        _output.collectUpdate(openAppAction.loading) { currentState, newData ->
            currentState.copy(isLoading = newData)
        }

        _output.collectUpdate(openAppAction.failure) { currentState, newData ->
            // TODO display the DS component called HelperMessage
            currentState.copy(errorMessage = newData.message)
        }

        _output.collectUpdate(openAppAction.success) { currentState, _ ->
            currentState.copy(navTarget = LauncherUiState.NavTarget.Login)
        }

        helperPrimaryActionInput.onCollect {
            openAppAction.launch()
        }
        openAppAction.launch()
    }

    override fun consume(consumable: Consumable<LauncherUiStateEffectKey, *>) {
        _output.update { it.clearEffect(consumable) }
    }

    override fun helperPrimaryAction() {
        helperPrimaryActionInput.launchEmit(Unit)
    }
}
