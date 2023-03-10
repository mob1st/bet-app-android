package br.com.mob1st.bet.core.ui.state
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.bet.core.logs.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import kotlin.Exception
import kotlin.coroutines.cancellation.CancellationException

/**
 * The project's base ViewModel, used to handle the asynchronous state changes in the UI.
 *
 * It applies the Unidirectional Data Flow design pattern, recommended by the JetPack Team, to
 * manage the state of the UI.
 *
 * @param Data It's typically the success operation of the asynchronous operation that provides the
 * state of the UI, while the UI event is the type of ui events this ViewModel handle.
 * @param UiEvent It's the types of events the UI can send to this ViewModel
 * @param initialState The initial state of the UI
 * @see AsyncState
 * **See also** [Ui-Layer](https://developer.android.com/topic/architecture/ui-layer)
 */
@Suppress("TooGenericExceptionCaught")
abstract class StateViewModel<Data, UiEvent>(
    initialState: AsyncState<Data>,
) : ViewModel(), KoinComponent {

    constructor(data: Data, loading: Boolean = true) : this(
        AsyncState(data = data, loading = loading)
    )

    protected val logger: Logger by inject()

    private val viewModelState = MutableStateFlow(initialState)

    /**
     * Provides the asynchronous state of the UI
     *
     * It uses a StateFlow to holds the last emitted state of the UI to be able to exposes it in a
     * immutable way.
     * **See also** [A safer way to collect flows from Android UIs]
     * (https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda)
     */
    val uiState: StateFlow<AsyncState<Data>> = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(STOP_TIMEOUT),
        viewModelState.value
    )

    val currentState get() = uiState.value
    val currentData get() = uiState.value.data

    /**
     * Indicates that the UI have consumed a message and it should be removed from the state of
     * the UI
     *
     * After removing this message, a new state will be triggered again to the UI without the
     * message
     * @param message the message consumed by UI
     * @param loading by default it removes the loading, so set this param if another behavior is
     * needed
     */
    open fun messageShown(
        message: SimpleMessage,
        loading: Boolean = false,
    ) = viewModelState.update { current ->
        current.removeMessage(message, loading)
    }

    /**
     * Uses the returned [Flow] as a [source] of states that will be emitted by the UI.
     *
     * It also catches the exceptions to display the default error message to the user, so for the
     * majorities of the cases no error handling is required.
     * This method caches the possible using the [Flow.catch] operator. Do the internal error
     * handling only in the cases spefic error states should be sent to the UI.
     *
     * @param source the block function that will provide the source. The current state of the UI in
     * the moment of this method is called will be sent by lambda parameter
     * @return The [Job] created in this operation. Use it to cancel the collection of the flow
     * provided by [source]
     */
    protected fun setSource(source: (current: AsyncState<Data>) -> Flow<AsyncState<Data>>): Job {
        return source(viewModelState.value)
            .catch {
                logger.e("setSource have failed", it)
                emit(viewModelState.value.failure())
            }
            .onEach { newState -> setAsync { newState } }
            .launchIn(viewModelScope)
    }

    /**
     * Changes the state of the UI.
     *
     * It creates a new coroutine, so suspend functions can be called safely. Every time this method
     * is called the UI will receive a new emission to be handled.
     * This method catches the possible errors thrown by [block] and a new state will be sent to the
     * UI with a default error message structure.
     *
     * @param block the block function that will be executed to manipulate the UI state
     * @return The [Job] created in this operation. Use it to cancel this asynchonous operation
     */
    protected fun setAsync(
        block: suspend CoroutineScope.(current: AsyncState<Data>) -> (AsyncState<Data>),
    ): Job = viewModelScope.launch {
        viewModelState.update { current ->
            try {
                block(current)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logger.e("setState have failed", e)
                current.failure()
            }
        }
    }

    /**
     * Turn on the loading on
     */
    protected fun loading() = viewModelState.update {
        it.loading()
    }

    /**
     * Set only the data, in a synchronous way. Use this method if no coroutines is required, such
     * as suspend functions or flow collections.
     * If some internal error happens, it will be catched and sent as default
     */
    protected fun setData(block: (current: Data) -> Data) = viewModelState.update {
        try {
            it.data(data = block(it.data))
        } catch (e: Exception) {
            Timber.e(e)
            it.failure()
        }
    }

    /**
     * Handle events from triggered by UI that can update its state
     *
     * This method typically maps events to Use Case calls, and can mutate the state of the UI more
     * then one time per call.
     *
     * Use [setAsync] or [setSource] in order to mutate the state of the UI
     */
    abstract fun fromUi(uiEvent: UiEvent)

    companion object {

        /**
         * The timeout to stop the emissions in case the UI is not more listening the [StateFlow]
         *
         * 5 seconds is the limit time for Android Not Responding errors, so using this magic number
         * we have guarantees the UI will receive new emissions
         * **See also**
         */
        private const val STOP_TIMEOUT = 5_000L
    }
}
