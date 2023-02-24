package br.com.mob1st.bet.core.utils.extensions

import androidx.compose.animation.Crossfade
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.mob1st.bet.core.tooling.androidx.TextData
import br.com.mob1st.bet.core.tooling.vm.next
import br.com.mob1st.bet.core.tooling.vm.onCollect
import br.com.mob1st.bet.core.tooling.flow.startsWith
import br.com.mob1st.bet.core.tooling.vm.actionFromFlow
import br.com.mob1st.bet.core.tooling.vm.trigger
import br.com.mob1st.bet.core.ui.ds.molecule.SnackState
import br.com.mob1st.bet.core.ui.ds.states.DelegateRefreshStateManager
import br.com.mob1st.bet.core.ui.ds.states.DelegateSnackStateManager
import br.com.mob1st.bet.core.ui.ds.states.RefreshStateManager
import br.com.mob1st.bet.core.ui.ds.states.SnackStateManager
import br.com.mob1st.bet.core.ui.ds.states.UiState
import br.com.mob1st.bet.core.ui.ds.states.updates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class ClickUseCase {
    operator fun invoke(value: Int): Flow<String> = flowOf(value).map { it.toString() }
}

class GetUseCase {
    operator fun invoke(): Flow<String> = flowOf("")
}

class MyViewModel(
    initialState: UiState<String>,
    getUseCase: GetUseCase,
    clickUseCase: ClickUseCase,
    snackStateManager: DelegateSnackStateManager,
    refreshStateManager: DelegateRefreshStateManager,
): ViewModel(),
    SnackStateManager by snackStateManager,
    RefreshStateManager by refreshStateManager {

    val output: StateFlow<UiState<String>>

    private val buttonClickInput = MutableSharedFlow<Unit>()

    private val getAction = actionFromFlow<String> {
        getUseCase()
    }
    private val clickAction = actionFromFlow<Int, String> {
        clickUseCase(it)
    }

    init {
        val mutableOutput = MutableStateFlow(initialState)
        val getFailure = getAction.failure.map { SnackState.generalFailure(TextData.Retry) }
        val clickFailure = clickAction.failure.map { SnackState.generalFailure() }
        mutableOutput
            .updates(
                loading = merge(getAction.loading, clickAction.loading),
                push = merge(getFailure, clickFailure),
                dismiss = snackStateManager.dismiss,
                success = getAction.success
            )

        buttonClickInput
            .onCollect {
                clickAction.trigger(0)
            }

        merge(refreshStateManager.refreshInput, snackStateManager.retryInput)
            .startsWith(Unit)
            .onCollect {
                getAction.trigger()
            }

        // provide to ui
        output = mutableOutput
    }

    fun click() {
        buttonClickInput.next(Unit)
    }

}

fun <T> MutableList<T>.putIf(element: T, predicate: (T) -> Boolean) {
    val index = indexOfFirst(predicate)
    if (index >= 0) {
        set(index, element)
    } else {
        add(element)
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Ui() {
    val vm = viewModel<MyViewModel>()
    val x by vm.output.collectAsStateWithLifecycle()
    Crossfade(targetState = x.mainContent != null) {
        if (it) {

        } else {

        }
    }
    Button(onClick = vm::click) {
        Text(text = x.mainContent.orEmpty())
    }
}