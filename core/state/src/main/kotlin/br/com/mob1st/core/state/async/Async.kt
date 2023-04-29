package br.com.mob1st.core.state.async

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A async operation implemented using the reactive paradigm approach.
 *
 * This is typically used by ViewModels to handle errors, loadings and successes states from asynchronous work.
 * It's useful to avoid crashes when some operation throws an exception, adding also a log.
 *
 * @param <I> the input to trigger the action
 * @param <O> the expected output for this action when it succeeds
 */
interface Async<I, O> {

    /**
     * Emits a boolean when the action starts or finishes (including when it fails).
     * true indicates the loading starts, false indicates it finishes.
     */
    val loading: Flow<Boolean>

    /**
     * Emits a Throwable when the action fails.
     */
    val failure: Flow<Throwable>

    /**
     * Emits the result of the action when it succeeds.
     */
    val success: Flow<O>

    /**
     * Triggers the action with the given input.
     * The state of this action will be emitted in the [loading], [failure] and [success] flows.
     */
    fun launch(input: I): Job
}

@OptIn(ExperimentalCoroutinesApi::class)
internal open class AsyncImpl<I, O>(
    private val scope: CoroutineScope,
    private val source: (I) -> Flow<O>,
) : Async<I, O> {

    private val actionInput = MutableSharedFlow<I>()
    private val asyncFlow = MutableStateFlow<AsyncState>(AsyncState.NotLaunchedYet)

    override val loading: Flow<Boolean> = asyncFlow.map { it.isLoading() }
    override val failure: Flow<Throwable> = asyncFlow.mapNotNull { it.failure() }
    override val success: Flow<O> = asyncFlow.mapNotNull { it.data<O>() }

    init {
        actionInput
            .flatMapLatest(::sourceFlow)
            .onEach { async ->
                asyncFlow.update { async }
            }
            .launchIn(scope)
    }

    private fun sourceFlow(input: I): Flow<AsyncState> = source(input)
        .map<O, AsyncState> {
            AsyncState.Success(it)
        }
        .catch {
            Timber.e(it)
            emit(AsyncState.Failure(it))
        }
        .onStart {
            emit(AsyncState.Loading)
        }

    override fun launch(input: I): Job = scope.launch {
        actionInput.emit(input)
    }
}

/**
 * Helper function to trigger an AsyncAction<Unit> without parameters.
 */
fun <O> Async<Unit, O>.launch(): Job = launch(Unit)

/**
 * Creates an [Async] from a [Flow] that receives an input.
 *
 * This is a convenience function to create Async<Unit, O>.
 * @param O provides the [Flow] that will be executed when the action is triggered
 * @param source provides the [Flow] that will be executed when the action is triggered
 */
@Suppress("FunctionName")
fun <O> ViewModel.Async(source: () -> Flow<O>): Async<Unit, O> {
    return Async<Unit, O> { source() }
}

/**
 * Creates an [Async] from a [Flow] that receives an input.
 *
 * @param I provides the input to trigger the action
 * @param O provides the [Flow] that will be executed when the action is triggered
 * @param source provides the [Flow] that will be executed when the action is triggered
 */
@Suppress("FunctionName")
fun <I, O> ViewModel.Async(source: (input: I) -> Flow<O>): Async<I, O> {
    return AsyncImpl(viewModelScope, source)
}
