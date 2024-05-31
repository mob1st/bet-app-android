package br.com.mob1st.core.state.managers

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class ResourceSharingStarted : SharingStarted {
    private val resourceStateFlow = MutableStateFlow(ResourceState.STOPPED)

    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> {
        return combine(
            subscriptionCount,
            resourceStateFlow,
        ) { count, state ->
            when (state) {
                ResourceState.STARTED ->
                    if (count > 0) {
                        SharingCommand.START
                    } else {
                        SharingCommand.STOP
                    }
                ResourceState.STOPPED -> {
                    delay(ANR_TIMEOUT)
                    SharingCommand.STOP
                }
            }
        }
    }

    fun startup() {
        resourceStateFlow.value = ResourceState.STARTED
    }

    fun shutdown() {
        resourceStateFlow.value = ResourceState.STOPPED
    }

    private enum class ResourceState {
        STARTED,
        STOPPED,
    }

    companion object {
        private const val ANR_TIMEOUT = 5_000L
    }
}
