package br.com.mob1st.bet.core.tooling.vm

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
import kotlinx.coroutines.launch
import timber.log.Timber

interface Action<I, O> {

    val loading: Flow<Boolean>
    val failure: Flow<Throwable>
    val success: Flow<O>

    fun trigger(input: I): Job
}

fun <I, O> CoroutineScope.actionFromFlow(
    source: (input: I) -> Flow<O>
): Action<I, O> {
    return ActionImpl(this, source)
}

fun <O> CoroutineScope.actionFromFlow(source: () -> Flow<O>): Action<Unit, O> {
    return ActionImpl(this) { source() }
}

@OptIn(ExperimentalCoroutinesApi::class)
private open class ActionImpl<I, O>(
    private val scope: CoroutineScope,
    private val source: (I) -> Flow<O>
) : Action<I, O> {

    private val trigger = MutableSharedFlow<I>()
    private val asyncFlow = MutableStateFlow<AsyncData>(AsyncData.NotTriggeredYet)

    override val loading: Flow<Boolean> = asyncFlow.mapNotNull { it.isLoading() }
    override val failure: Flow<Throwable> = asyncFlow.mapNotNull { it.failure() }
    override val success: Flow<O> = asyncFlow.mapNotNull { it.data<O>() }

    init {
        trigger.flatMapLatest { input ->
            source(input)
                .map<O, AsyncData> {
                    AsyncData.Success(it)
                }
                .catch {
                    Timber.e(it)
                    emit(AsyncData.Failure(it))
                }
                .onStart {
                    emit(AsyncData.Loading)
                }
                .onEach {
                    asyncFlow.emit(it)
                }
        }.launchIn(scope)
    }

    override fun trigger(input: I): Job = scope.launch {
        trigger.emit(input)
    }
}

fun <O> Action<Unit, O>.trigger() = trigger(Unit)
