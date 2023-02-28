package br.com.mob1st.bet.core.ui.ds.organisms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import br.com.mob1st.bet.core.tooling.vm.stateInRetained
import br.com.mob1st.bet.core.ui.ds.molecule.SnackState
import br.com.mob1st.bet.core.ui.ds.states.DelegateRefreshStateInput
import br.com.mob1st.bet.core.ui.ds.states.DelegateSnackStateInput
import br.com.mob1st.bet.core.ui.ds.states.RefreshStateInput
import br.com.mob1st.bet.core.ui.ds.states.SnackStateInput
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

interface PageStateManager<Data> : RefreshStateInput, SnackStateInput {

    val stateOutput: StateFlow<Either<PageState.Empty, Data>>

    val snacksOutput: StateFlow<List<SnackState<*>>>

    val helperOutput: StateFlow<List<PageState.Helper>>
}

open class PageStateViewModel<Data>(
    private val initialValue: PageState.Empty = PageState.Empty(),
    protected val refreshStateInput: DelegateRefreshStateInput = DelegateRefreshStateInput(),
    protected val snackStateInput: DelegateSnackStateInput = DelegateSnackStateInput()
) : ViewModel(),
    PageStateManager<Data>,
    RefreshStateInput by refreshStateInput,
    SnackStateInput by snackStateInput {

    private val _output = MutableStateFlow(initialValue)

    protected val data = MutableSharedFlow<Data>()
    protected val empty = MutableSharedFlow<PageState.Empty>()

    protected val refreshingInput = MutableSharedFlow<Boolean>()

    protected val helperFlow = MutableSharedFlow<PageState.Helper>()
    protected val generalFailureFlow = MutableSharedFlow<Throwable>()

    final override val helperOutput: StateFlow<PersistentList<PageState.Helper>>
    init {
        refreshStateInput.scope = viewModelScope
        snackStateInput.scope = viewModelScope

        val emptyFlow = stateOutput.filterIsInstance<Either.Left<PageState.Empty>>().map { it.value }
        val dataFow = stateOutput.filterIsInstance<Either.Right<Data>>().map { it.value }

        val generalHelperFailure = generalFailureFlow
            .filter { stateOutput.value.isLeft() }
            .map { PageState.Helper.generalFailure() }

        helperOutput = merge(helperFlow, generalHelperFailure)
            .map { persistentListOf(it) }
            .stateInRetained(persistentListOf())
    }

    protected fun updatePage(
        failure: Flow<Throwable> = emptyFlow(),
        loading: Flow<Boolean> = emptyFlow(),
        poll: Flow<SnackState<*>> = emptyFlow(),
        data: Flow<Data> = emptyFlow()
    ) {
        /*
        combine(failure, loading, poll, data) { f, l, p, d ->
            ""
        }.stateInRetained(initialValue)

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

         */
    }

    private fun <T> updateEmpty(
        source: Flow<T>,
        transform: suspend (state: PageState.Empty, value: T) -> PageState
    ): Job = updateState(source = source, empty = transform)

    private fun <T> updateHelper(
        source: Flow<T>,
        transform: suspend (state: PageState.Helper, value: T) -> PageState
    ): Job = updateState(source = source, helper = transform)

    protected fun <T> updateMain(
        source: Flow<T>,
        transform: suspend (state: PageState.Main<Data>, value: T) -> PageState
    ): Job = updateState(source = source, main = transform)

    protected fun <T> updateMainData(
        source: Flow<T>,
        transform: suspend (data: Data, value: T) -> Data
    ): Job = updateMain(source) { state, value ->
        state.copy(data = transform(state.data, value))
    }

    private fun <T> updateState(
        source: Flow<T>,
        empty: suspend (state: PageState.Empty, T) -> PageState = { s, _ -> s },
        helper: suspend (state: PageState.Helper, T) -> PageState = { s, _ -> s },
        main: suspend (state: PageState.Main<Data>, T) -> PageState = { s, _ -> s }
    ): Job = source.onEach { value ->
        _output.update { state ->
//            when (state) {
//                is PageState.Empty -> empty(state, value)
//                is PageState.Helper -> helper(state, value)
//                is PageState.Main -> main(state, value)
//            }
            state
        }
    }.launchIn(viewModelScope)

    final override val stateOutput: StateFlow<Either<PageState.Empty, Data>>
        get() = TODO("Not yet implemented")
    override val snacksOutput: StateFlow<List<SnackState<*>>>
        get() = TODO("Not yet implemented")
}
