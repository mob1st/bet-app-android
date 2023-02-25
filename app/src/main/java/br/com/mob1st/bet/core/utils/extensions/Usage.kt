package br.com.mob1st.bet.core.utils.extensions

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.mob1st.bet.core.tooling.flow.startsWith
import br.com.mob1st.bet.core.tooling.vm.actionFromFlow
import br.com.mob1st.bet.core.tooling.vm.next
import br.com.mob1st.bet.core.tooling.vm.trigger
import br.com.mob1st.bet.core.ui.ds.molecule.SnackBar
import br.com.mob1st.bet.core.ui.ds.molecule.SnackState
import br.com.mob1st.bet.core.ui.ds.organisms.PageState
import br.com.mob1st.bet.core.ui.ds.organisms.PageStateViewModel
import br.com.mob1st.bet.core.ui.ds.states.DelegateRefreshStateManager
import br.com.mob1st.bet.core.ui.ds.states.DelegateSnackStateInput
import br.com.mob1st.bet.core.ui.ds.states.RefreshStateInput
import br.com.mob1st.bet.core.ui.ds.states.SnackStateInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ClickUseCase {
    operator fun invoke(value: Int): Flow<String> = flowOf(value).map { it.toString() }
}

class GetUseCase {
    operator fun invoke(): Flow<String> = flowOf("")
}

@Immutable
data class UsageData(
    val content: String,
    val clickRunning: Boolean = false,
)

class MyViewModel(
    getUseCase: GetUseCase,
    clickUseCase: ClickUseCase,
    snackStateManager: DelegateSnackStateInput,
    refreshStateManager: DelegateRefreshStateManager,
): PageStateViewModel<UsageData>(),
    SnackStateInput by snackStateManager,
    RefreshStateInput by refreshStateManager {

    private val buttonClickInput = MutableSharedFlow<Unit>()
    private val textInput = MutableSharedFlow<String>()
    private val getAction = viewModelScope.actionFromFlow<String> {
        getUseCase()
    }
    private val clickAction = viewModelScope.actionFromFlow<Int, String> {
        clickUseCase(it)
    }

    init {
        updatePage(
            failure = getAction.failure,
            loading = getAction.loading,
            poll = snackStateManager.poll,
            data = getAction.success.map { UsageData(content = it) },
        )

        updateMainData(getAction.success) { data, value ->
            data.copy(content = value)
        }

        updateMain(clickAction.failure) { state, _ ->
            state.offer(SnackState.generalFailure())
        }

        updateMainData(clickAction.loading) { data, value ->
            data.copy(clickRunning = value)
        }

        merge(refreshStateManager.refreshInput, snackStateManager.actionPerformedInput)
            .startsWith(Unit)
            .onEach {
                getAction.trigger()
            }
            .launchIn(viewModelScope)

        buttonClickInput
            .onEach {
                clickAction.trigger(0)
            }
            .launchIn(viewModelScope)
        updateMainData(textInput) { data, value ->
            if (value.length < 5) {
                data.copy(clickRunning = true)
            } else {
                data
            }
        }

    }

    fun click() {
        buttonClickInput.next(Unit)
    }

}

@Composable
fun Ui() {
    val vm = viewModel<MyViewModel>()

    val state by vm.output.collectAsStateWithLifecycle()
    when (val st = state) {
        is PageState.Empty -> { }
        is PageState.Helper -> { }
        is PageState.Main -> {
            st.peek()?.let {
                SnackBar(
                    state = it,
                    onDismiss = vm::dismiss,
                    onActionPerformed = vm::actionPerformed
                )
            }
            Text(text = st.data.content)
        }
    }

    Button(onClick = vm::click) {

    }
}