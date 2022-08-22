package br.com.mob1st.bet.core.ui

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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
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

    fun consumeEvent(event: SingleShotEvent<*>) {
        viewModelState.update { current ->
            current.consumeEvent(event)
        }
    }

    fun tryAgain(sourceEvent: SingleShotEvent<*>) {
        viewModelState.update {
            it.loading(loading = true, ).consumeEvent(sourceEvent)
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

