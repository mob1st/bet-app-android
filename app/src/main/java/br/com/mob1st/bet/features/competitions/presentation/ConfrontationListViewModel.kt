package br.com.mob1st.bet.features.competitions.presentation

import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.competitions.domain.Confrontation
import br.com.mob1st.bet.features.competitions.domain.GetConfrontationListUseCase
import javax.annotation.concurrent.Immutable

class CreateGuessUseCase() {
    suspend operator fun invoke(){

    }
}

@Immutable
data class ConfrontationData(
    val confrontations: List<Confrontation> = emptyList(),
    val customErrorVisible: Boolean = false
)

sealed class ConfrontationUiEvent {
    data class CreateGuess(val team1Winner: Boolean) : ConfrontationUiEvent()
    object TryAgain : ConfrontationUiEvent()
}

class ConfrontationListViewModel(
    private val getConfrontationListUseCase: GetConfrontationListUseCase,
    private val createGuessUseCase: CreateGuessUseCase,
) : StateViewModel<ConfrontationData, ConfrontationUiEvent>(
    AsyncState(data = ConfrontationData(), loading = true)
) {

    init {
        setState {
            it.data(data = it.data.copy(confrontations = getConfrontationListUseCase()))
        }
    }

    override fun fromUi(uiEvent: ConfrontationUiEvent) {
        when (uiEvent) {
            is ConfrontationUiEvent.CreateGuess -> callCreateGuess()
            ConfrontationUiEvent.TryAgain -> TODO()
        }
    }

    private suspend fun callCreateGuess() {
        setState {
            it.loading()
        }
        setState { current: AsyncState<ConfrontationData> ->


        }
    }
}