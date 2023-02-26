package br.com.mob1st.bet.core.ui.ds.organisms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.bet.core.tooling.androidx.TextData
import br.com.mob1st.bet.core.ui.ds.molecule.SnackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

interface PageStateManager<Data> {
    val output: StateFlow<PageState<Data>>
}

abstract class PageStateViewModel<Data>(
    initialValue: PageState<Data> = PageState.Empty(),
) : ViewModel(), PageStateManager<Data> {

    private val _output = MutableStateFlow(initialValue)
    override val output: StateFlow<PageState<Data>> = _output.asStateFlow()
    protected fun updatePage(
        failure: Flow<Throwable> = emptyFlow(),
        loading: Flow<Boolean> = emptyFlow(),
        poll: Flow<SnackState<*>> = emptyFlow(),
        data: Flow<Data> = emptyFlow()
    ) {
        updateEmpty(failure) { _, _ ->
            PageState.Helper.generalFailure()
        }

        updateEmpty(loading) { state, value ->
            state.copy(loading = value)
        }

        updateEmpty(data) { _, value ->
            PageState.Main(data = value)
        }

        updateHelper(loading) { state, value ->
            state.loading(value)
        }

        updateHelper(data) { _, value ->
            PageState.Main(value)
        }

        updateMain(poll) { state, _ ->
            state.poll()
        }

        updateMain(loading) { state, value ->
            state.copy(refreshing = value)
        }

        updateMain(failure) { state, _ ->
            state.offer(SnackState.generalFailure(TextData.Retry))
        }
    }

    private fun <T> updateEmpty(
        source: Flow<T>,
        transform: suspend (state: PageState.Empty<Data>, value:T) -> PageState<Data>
    ): Job = updateState(source = source, empty = transform)

    private fun <T> updateHelper(
        source: Flow<T>,
        transform: suspend (state: PageState.Helper<Data>, value: T) -> PageState<Data>
    ): Job = updateState(source = source, helper = transform)

    protected fun <T> updateMain(
        source: Flow<T>,
        transform: suspend (state: PageState.Main<Data>, value:T) -> PageState<Data>
    ): Job = updateState(source = source, main = transform)
    
    protected fun <T> updateMainData(
        source: Flow<T>,
        transform: suspend (data: Data, value:T) -> Data
    ): Job = updateMain(source) { state, value ->
        state.copy(data = transform(state.data, value))
    }
    
    private fun <T> updateState(
        source: Flow<T>,
        empty: suspend (state: PageState.Empty<Data>, T) -> PageState<Data> = { s, _ -> s },
        helper: suspend (state: PageState.Helper<Data>, T) -> PageState<Data> = { s, _ -> s },
        main: suspend (state: PageState.Main<Data>, T) -> PageState<Data> = { s, _ -> s },
    ): Job = source.onEach { value ->
        _output.update { state ->
            when (state) {
                is PageState.Empty -> empty(state, value)
                is PageState.Helper -> helper(state, value)
                is PageState.Main -> main(state, value)
            }
        }
    }.launchIn(viewModelScope)
    
}