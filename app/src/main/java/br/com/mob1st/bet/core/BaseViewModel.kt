package br.com.mob1st.bet.core

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.logs.DebuggableException
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
import java.util.UUID

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
                logger.w("some error happened while fetching data", t)
                emit(viewModelState.updateAndGet { it.error(t) })
            }
            .launchIn(viewModelScope)
    }

    abstract fun dataFlow(): Flow<T>

    companion object {
        private const val STOP_TIMEOUT = 5_000L
    }

}

@Immutable
data class UiState<T>(
    val data: T,
    val loading: Boolean = false,
    val singleShotEvents: List<SingleShotEvent<*>> = emptyList(),
) {

    fun consumeEvent(event: SingleShotEvent<*>): UiState<T> {
        return copy(singleShotEvents = singleShotEvents.filterNot { it.id == event.id })
    }

    fun data(data: T, loading: Boolean = false): UiState<T>  {
        return copy(
            loading = loading,
            data = data,
        )
    }

    fun error(throwable: Throwable, loading: Boolean = false): UiState<T> {
        return copy(
            singleShotEvents = singleShotEvents + throwable.toErrorMessage(),
            loading = loading
        )
    }

    fun loading(loading: Boolean = true): UiState<T> {
        return copy(loading = loading)
    }
}

@Immutable
interface SingleShotEvent<T> {
    val id: UUID
    val data: T
}

@Immutable
data class GeneralErrorMessage(
    @StringRes override val data: Int = R.string.app_name,
    override val id: UUID = UUID.randomUUID(),
) : SingleShotEvent<Int>

fun Throwable.toErrorMessage(): GeneralErrorMessage {
    return if (this is DebuggableException) {
        GeneralErrorMessage(errorCode)
    } else {
        GeneralErrorMessage()
    }
}