package br.com.mob1st.bet.features.bet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import br.com.mob1st.bet.core.ui.StateViewModel
import br.com.mob1st.bet.core.ui.UiState
import br.com.mob1st.bet.features.auth.OpenAppUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel
import java.util.UUID

@KoinViewModel
class BetViewModel(
    private val openAppUseCase: OpenAppUseCase,
    private val getBetListUseCase: GetBetListUseCase
) : StateViewModel<List<Guess>, BetUiEvent>(UiState(data = emptyList(), loading = true)) {

    init {
        setSource { currentState ->

            getBetListUseCase().map { currentState.data(it) }
        }
    }

    override suspend fun handleEvent(uiEvent: BetUiEvent) {
        when (uiEvent) {
            is BetUiEvent.AddMember -> TODO()
            is BetUiEvent.GeneralError -> TODO()
            is BetUiEvent.RemoveMember -> TODO()
            BetUiEvent.Paginate -> TODO()
        }
    }

    private fun onAddMember(addMember: BetUiEvent.AddMember) {
        setState {
            it.loading()
        }
        setState {
            delay(1)
            it.data(emptyList())
        }
    }



}

sealed class BetUiEvent {
    data class GeneralError(val text: String, val id: UUID, val data: String) : BetUiEvent()
    data class AddMember(val name: String) : BetUiEvent()
    data class RemoveMember(val id: String) : BetUiEvent()
    object Paginate : BetUiEvent()
}

class BetFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}