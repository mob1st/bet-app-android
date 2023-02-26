package br.com.mob1st.bet.features.launch.presentation

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.launch.domain.LaunchAppUseCase
import br.com.mob1st.bet.features.profile.data.Subscription
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LauncherViewModel(
    private val launchAppUseCase: LaunchAppUseCase
) : StateViewModel<LaunchData, LauncherUiEvent>(AsyncState(LaunchData(), loading = true)) {

    init {
        triggerUseCase()
    }

    override fun fromUi(uiEvent: LauncherUiEvent) {
        messageShown((uiEvent as LauncherUiEvent.TryAgain).simpleMessage, loading = true)
        triggerUseCase()
    }

    private fun triggerUseCase() {
        setAsync {
            val subscription = launchAppUseCase()
            it.data(LaunchData.subscription.set(it.data, subscription))
        }
    }
}

@Immutable
@optics
data class LaunchData(val subscription: Subscription? = null) {
    companion object
}

sealed class LauncherUiEvent {
    data class TryAgain(val simpleMessage: SimpleMessage) : LauncherUiEvent()
}
