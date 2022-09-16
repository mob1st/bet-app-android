package br.com.mob1st.bet.features.competitions.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import arrow.optics.optics
import br.com.mob1st.bet.core.ui.state.FetchedData
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.competitions.domain.CompetitionRepository
import br.com.mob1st.bet.features.competitions.domain.Confrontation
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.annotation.KoinViewModel

@optics
data class ConfrontationData(
    val entry: CompetitionEntry,
    val confrontations: List<Confrontation> = emptyList(),
    val selected: Int?  = null,
) : FetchedData {

    val detail: Confrontation? get() = selected?.let { confrontations[selected] }

    override fun hasData(): Boolean = confrontations.isNotEmpty()

    companion object
}

sealed class ConfrontationUiEvent {
    data class TryAgain(val message: SimpleMessage) : ConfrontationUiEvent()
    data class SetSelection(val index: Int?) : ConfrontationUiEvent()
}

@KoinViewModel
class ConfrontationListViewModel(
    entry: CompetitionEntry,
    private val repository: CompetitionRepository,
    private val savedState: SavedStateHandle
) : StateViewModel<ConfrontationData, ConfrontationUiEvent>(ConfrontationData(entry)){

    init {
        load()
        savedState.getStateFlow<Int?>(SELECTED, null)
            .filter { selected -> selected != currentData.selected }
            .onEach { selected ->
                logger.d("ptest on select $selected")
                setData { it.copy(selected = selected) }
            }
            .launchIn(viewModelScope)
    }

    override fun fromUi(uiEvent: ConfrontationUiEvent) {
        when (uiEvent) {
            is ConfrontationUiEvent.TryAgain -> tryAgain(uiEvent.message)
            is ConfrontationUiEvent.SetSelection -> savedState[SELECTED] = uiEvent.index
        }
    }

    private fun tryAgain(message: SimpleMessage) {
        messageShown(message, loading = true)
        load()
    }

    private fun load() {
        setAsync {
            val confrontations = repository.getConfrontationsBy(it.data.entry.id)
            logger.d("fetch ${confrontations.size} confrontations")
            it.data(
                ConfrontationData.confrontations.set(it.data, confrontations),
            )
        }
    }

    companion object {
        private const val SELECTED = "index"
    }
}