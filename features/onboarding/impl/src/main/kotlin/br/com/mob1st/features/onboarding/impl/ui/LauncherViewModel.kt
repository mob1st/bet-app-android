package br.com.mob1st.features.onboarding.impl.ui

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.async.Async
import br.com.mob1st.core.state.async.launch
import br.com.mob1st.core.state.extensions.collectUpdate
import br.com.mob1st.core.state.extensions.launchEmit
import br.com.mob1st.core.state.extensions.onCollect
import br.com.mob1st.features.onboarding.impl.domain.OpenAppUseCase
import br.com.mob1st.features.onboarding.impl.domain.SplashDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

/**
 * A ViewModel that manages of the Launcher screen.
 *
 * @param openAppUseCase A use case triggered when the app opens.
 */
@KoinViewModel
class LauncherViewModel(
    openAppUseCase: OpenAppUseCase,
) : ViewModel(), LauncherUiContract {
    // output state
    private val _uiStateOutput = MutableStateFlow(LauncherUiState())
    override val uiStateOutput: StateFlow<LauncherUiState> = _uiStateOutput.asStateFlow()

    // inputs
    private val helperPrimaryActionInput = MutableSharedFlow<Unit>()

    // actions
    private val openAppAction =
        Async<SplashDestination> {
            openAppUseCase()
        }

    init {
        _uiStateOutput.collectUpdate(openAppAction.loading) { currentState, newData ->
            currentState.copy(isLoading = newData)
        }

        _uiStateOutput.collectUpdate(openAppAction.failure) { currentState, newData ->
            // TODO display the DS component called HelperMessage
            currentState.copy(errorMessage = newData.message)
        }

        _uiStateOutput.collectUpdate(openAppAction.success) { currentState, splashDestination ->
            currentState.copy(navTarget = LauncherNavTarget.of(splashDestination))
        }

        helperPrimaryActionInput.onCollect {
            openAppAction.launch()
        }
        openAppAction.launch()
    }

    override fun helperPrimaryAction() {
        helperPrimaryActionInput.launchEmit(Unit)
    }
}
