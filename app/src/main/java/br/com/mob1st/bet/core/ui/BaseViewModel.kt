package br.com.mob1st.bet.core.ui

import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.bet.core.logs.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

abstract class BaseViewModel<T>(initialState: UiState<T>) : ViewModel(), KoinComponent {

    private val logger: Logger by inject {
        parametersOf("${this.javaClass.simpleName}(${hashCode()})")
    }

    constructor(initialData: T, loading: Boolean = true) : this(
        initialState = UiState(data = initialData, loading = loading)
    )

    private val viewModelState = MutableStateFlow(initialState)
    val uiState = viewModelState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
        initialValue = viewModelState.value
    )

    val currentState get() =  viewModelState.value

    private var fetchJob: Job? = null

    fun tryAgain(sourceEvent: SingleShot<*>) {
        viewModelState.update {
            it.loading(loading = true, ).removeSingleShot(sourceEvent)
        }
    }

    protected fun fetchData() {
        fetchJob?.cancel()
        fetchJob = dataFlow()
            .onStart {
                logger.v("fetch data started")
            }
            .map { data ->
                logger.v("on receive data")
                viewModelState.updateAndGet { it.data(data) }
            }
            .catch { t ->
                logger.e("some error happened while fetching data", t)
                emit(viewModelState.updateAndGet { it.error(t) })
            }
            .launchIn(viewModelScope)
    }

    abstract fun dataFlow(): Flow<T>

    companion object {
        private const val STOP_TIMEOUT = 5_000L
    }

}

abstract class StateViewModel<Data, UiEvent>(initialState: UiState<Data>) : ViewModel(), KoinComponent {

    private val logger: Logger by inject {
        parametersOf("${this.javaClass.simpleName}(${hashCode()})")
    }

    private var sourceJob: Job? = null

    private val viewModelState = MutableStateFlow(initialState)
    val uiState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        viewModelState.value,
    )

    fun consumeSingleShot(event: SingleShot<*>) {
        setState { current ->
            current.removeSingleShot(event)
        }
    }

    fun ui(event: UiEvent) {
        viewModelScope.launch {
            try {
                handleEvent(event)
            } catch (cause: Throwable) {
                logger.e("the ui event $event triggered an error", cause)
                setState { it.error(cause) }
            }
        }
    }

    protected fun setSource(source: (UiState<Data>) -> Flow<UiState<Data>>): Job {
        return source(viewModelState.value)
            .catch { cause: Throwable ->
                logger.e("the source flow has failed", cause)
                emit(viewModelState.value.error(cause))
            }
            .onEach { newState -> setState { newState }}
            .launchIn(viewModelScope)
    }

    protected fun setState(block: suspend (UiState<Data>) -> (UiState<Data>)): Job {
        return viewModelScope.launch {
            viewModelState.update { state ->
                block(state)
            }
        }
    }

    abstract suspend fun handleEvent(uiEvent: UiEvent)
}

@Composable
fun TryAgainHelper() {
    Dialog(onDismissRequest = { /*TODO*/ }) {

    }
}

@Composable
fun SnackbarEvent() {
    Snackbar(
        action = {  },
    ) {

    }
}