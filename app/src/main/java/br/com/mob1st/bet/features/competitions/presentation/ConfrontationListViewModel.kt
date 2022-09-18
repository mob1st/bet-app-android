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

/**
 * The data holder used in the Confrontations List and Confrontation Detail
 */
@optics
data class ConfrontationData(
    val entry: CompetitionEntry,
    val confrontations: List<Confrontation> = emptyList(),
    val hasFinished: Boolean = false,
    val selected: Int?  = null,
) : FetchedData {

    /*
    Operators for UI.
     */
    val detail: Confrontation? get() = selected?.let { confrontations.getOrNull(selected) }
    val progress: Float get() = ((selected!! + 1) / confrontations.size).toFloat()
    val hasNext get() = selected != null && selected + 1 > confrontations.size
    val isLast get() = selected != null && selected == confrontations.lastIndex

    override fun hasData(): Boolean = confrontations.isNotEmpty()

    companion object
}

/**
 * All UI events the confrontation list and the confrontation detail can trigger
 */
sealed class ConfrontationUiEvent {
    data class TryAgain(val message: SimpleMessage) : ConfrontationUiEvent()
    data class SetSelection(val index: Int?) : ConfrontationUiEvent()
    object GetNext : ConfrontationUiEvent()
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
                setData { it.copy(selected = selected, hasFinished = false) }
            }
            .launchIn(viewModelScope)
    }

    override fun fromUi(uiEvent: ConfrontationUiEvent) {
        when (uiEvent) {
            is ConfrontationUiEvent.TryAgain -> tryAgain(uiEvent.message)
            is ConfrontationUiEvent.SetSelection -> savedState[SELECTED] = uiEvent.index
            ConfrontationUiEvent.GetNext -> getNext()
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

    private fun getNext() {
        setData { current ->
            if (current.hasNext) {
                val selected = checkNotNull(current.selected) {
                    "If hasNext returns true so selected should be not null"
                }
                ConfrontationData.selected.set(current, selected + 1)
            } else {
                ConfrontationData.hasFinished.set(current, true)
            }
        }
    }

    companion object {
        private const val SELECTED = "index"
    }
}