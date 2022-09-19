package br.com.mob1st.bet.features.launch.presentation

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.launch.domain.LaunchAppUseCase
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LauncherViewModel(
    private val launchAppUseCase: LaunchAppUseCase,
) : StateViewModel<LaunchData, LauncherUiEvent>(AsyncState(LaunchData(), loading = true)){

    init {
        triggerUseCase()
    }

    override fun fromUi(uiEvent: LauncherUiEvent) {
        messageShown((uiEvent as LauncherUiEvent.TryAgain).simpleMessage, loading = true)
        triggerUseCase()
    }

    private fun triggerUseCase() {
        setAsync {
            val entry: CompetitionEntry = launchAppUseCase()
            it.data(data = LaunchData.competitionEntry.set(it.data, entry))
        }
    }

}

@Immutable
@optics
data class LaunchData(val competitionEntry: CompetitionEntry? = null) {
    companion object
}

sealed class LauncherUiEvent {
    data class TryAgain(val simpleMessage: SimpleMessage) : LauncherUiEvent()
}