package br.com.mob1st.bet.core.tooling.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

interface Action<I, O> {

    val loading: Flow<Boolean>
    val failure: Flow<Throwable>
    val success: Flow<O>

    fun trigger(input: I): Job

}

context(ViewModel)
fun <I, O> ViewModel.actionFromFlow(source: (input: I) -> Flow<O>): Action<I, O> {
    return ActionImpl(source)
}

context(ViewModel)
fun <O> ViewModel.actionFromFlow(source: () -> Flow<O>): Action<Unit, O> {
    return ActionImpl { source() }
}

context(ViewModel)
@OptIn(ExperimentalCoroutinesApi::class)
private open class ActionImpl<I, O>(
    private val source: (I) -> Flow<O>
) : Action<I, O> {

    private val trigger = MutableSharedFlow<I>()
    private val asyncFlow = MutableStateFlow<AsyncData>(AsyncData.NotTriggeredYet)

    override val loading: Flow<Boolean> = asyncFlow.mapNotNull { it.isLoading() }
    override val failure: Flow<Throwable> = asyncFlow.mapNotNull { it.throwable() }
    override val success: Flow<O> = asyncFlow.mapNotNull { it.data<O>() }

    init {
        trigger.flatMapLatest { input ->
            source(input)
                .map<O, AsyncData> { AsyncData.Success(it) }
                .catch {
                    Timber.e(it)
                    emit(AsyncData.Failure(it))
                }
                .onStart {
                    emit(AsyncData.Loading)
                }
        }.launchIn(viewModelScope)
    }

    override fun trigger(input: I): Job = viewModelScope.launch {
        trigger.emit(input)
    }
}

fun <O> Action<Unit, O>.trigger() = trigger(Unit)