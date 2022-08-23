package br.com.mob1st.bet.core.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.timber.DebuggableException
import java.util.UUID

@Immutable
data class UiState<T>(
    val data: T,
    val loading: Boolean = false,
    val singleShotEvents: List<SingleShotEvent<*>> = emptyList(),
) {

    fun consumeEvent(event: SingleShotEvent<*>): UiState<T> {
        return copy(singleShotEvents = removeEvent(event))
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

    fun tryAgain(sourceEvent: SingleShotEvent<*>): UiState<T> {
        return copy(loading = true, singleShotEvents = removeEvent(sourceEvent))
    }

    private fun removeEvent(event: SingleShotEvent<*>): List<SingleShotEvent<*>> {
        return singleShotEvents.filterNot { it.id == event.id }
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