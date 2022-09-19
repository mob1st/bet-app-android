package br.com.mob1st.bet.features.competitions.presentation

import arrow.optics.optics
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.competitions.domain.CompetitionRepository
import br.com.mob1st.bet.features.competitions.domain.Confrontation
import org.koin.android.annotation.KoinViewModel

@optics
data class ConfrontationData(
    val entry: CompetitionEntry,
    val confrontations: List<Confrontation> = emptyList(),
) {
    companion object
}

@KoinViewModel
class ConfrontationListViewModel(
    entry: CompetitionEntry,
    private val repository: CompetitionRepository
) : StateViewModel<ConfrontationData, Nothing>(AsyncState(data = ConfrontationData(entry), loading = true)){

    init {
        setState {
            val confrontations = repository.getConfrontationsBy(it.data.entry.id)
            logger.d("fetch ${confrontations.size} confrontations")
            it.data(
                ConfrontationData.confrontations.set(it.data, confrontations),
            )
        }
    }

    override fun fromUi(uiEvent: Nothing) {
        TODO("Not yet implemented")
    }
}