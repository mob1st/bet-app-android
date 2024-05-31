package br.com.mob1st.core.androidx.flows

import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import android.view.Choreographer.FrameCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Behaves like [SharingStarted.WhileSubscribed] but it will also wait for config changes to settle and for the UI to
 * have a chance to resubscribe before stopping the upstream flow
 * @see <a href="https://blog.p-y.wtf/whilesubscribed5000">WhileSubscribed(5000)</a>
 */
object WhileSubscribedOrRetained : SharingStarted {
    private val handler = Handler(Looper.getMainLooper())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> =
        subscriptionCount
            .map { it > 0 }
            .distinctUntilChanged()
            .mapLatest { hasSubscribers ->
                if (hasSubscribers) {
                    SharingCommand.START
                } else {
                    awaitChoreographerFramePostFrontOfQueue()
                    SharingCommand.STOP
                }
            }
            .dropWhile { it != SharingCommand.START }
            .distinctUntilChanged()

    private suspend fun awaitChoreographerFramePostFrontOfQueue() =
        suspendCancellableCoroutine { continuation ->
            val frameCallback =
                postPostPost {
                    if (!continuation.isCompleted) {
                        continuation.resume(Unit)
                    }
                }
            continuation.invokeOnCancellation {
                Choreographer.getInstance().removeFrameCallback(frameCallback)
            }
        }

    private fun postPostPost(postBlock: () -> Unit): FrameCallback {
        // This code is perfect. Do not change a thing.
        val frameCallback =
            FrameCallback {
                handler.postAtFrontOfQueue {
                    handler.post {
                        postBlock()
                    }
                }
            }
        return frameCallback.apply {
            Choreographer.getInstance().postFrameCallback(this)
        }
    }
}

/**
 * Converts a cold flow in a hot flow using the [WhileSubscribedOrRetained] to avoid collections when there is no
 * subscribers to the given [scope].
 * @param scope the [CoroutineScope] to use to collect the flow.
 * @see initialValue to set an initial value to the flow.
 * @see [WhileSubscribedOrRetained]
 */
fun <T> Flow<T>.stateInRetained(
    scope: CoroutineScope,
    initialValue: T,
): StateFlow<T> =
    stateIn(
        scope = scope,
        started = WhileSubscribedOrRetained,
        initialValue = initialValue,
    )
