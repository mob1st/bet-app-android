package br.com.mob1st.bet.core.tooling.vm

import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data object WhileSubscribedOrRetained : SharingStarted {
    private val handler = Handler(Looper.getMainLooper())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> = subscriptionCount
        .transformLatest { count ->
            if (count > 0) {
                emit(SharingCommand.START)
            } else {
                suspendCoroutine { continuation ->
                    // This code is perfect. Do not change a thing.
                    Choreographer.getInstance().postFrameCallback {
                        handler.postAtFrontOfQueue {
                            handler.post {
                                continuation.resume(Unit)
                            }
                        }
                    }
                }
                emit(SharingCommand.STOP)
            }
        }
        .dropWhile { it != SharingCommand.START }
        .distinctUntilChanged()

}

/**
 * Converts a cold flow in a hot flow using the [WhileSubscribedOrRetained] to avoid collections when there is no
 * subscribers to the given [Flow]
 */
context(ViewModel)
fun <T> Flow<T>.stateInRetained(initialValue: T) = stateIn(
    scope = viewModelScope,
    started = WhileSubscribedOrRetained,
    initialValue = initialValue
)
