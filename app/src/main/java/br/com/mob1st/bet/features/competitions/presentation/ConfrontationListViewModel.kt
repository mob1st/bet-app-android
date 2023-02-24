package br.com.mob1st.bet.features.competitions.presentation

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import arrow.optics.optics
import br.com.mob1st.bet.core.ui.state.FetchedData
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.core.tooling.ktx.Duo
import br.com.mob1st.bet.core.tooling.ktx.Node
import br.com.mob1st.bet.features.competitions.domain.AnswerAggregation
import br.com.mob1st.bet.features.competitions.domain.CompetitionRepository
import br.com.mob1st.bet.features.competitions.domain.Confrontation
import br.com.mob1st.bet.features.competitions.domain.ConfrontationForGuess
import br.com.mob1st.bet.features.competitions.domain.Contest
import br.com.mob1st.bet.features.competitions.domain.Duel
import br.com.mob1st.bet.features.competitions.domain.Guess
import br.com.mob1st.bet.features.competitions.domain.IntScores
import br.com.mob1st.bet.features.competitions.domain.MatchWinner
import br.com.mob1st.bet.features.competitions.domain.PlaceGuessUseCase
import br.com.mob1st.bet.features.competitions.domain.WinnerAnswers
import br.com.mob1st.bet.features.profile.data.Subscription
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.koin.android.annotation.KoinViewModel

/**
 * The data holder used in the Confrontations List and Confrontation Detail
 */
@optics
data class ConfrontationData(
    val subscription: Subscription,
    val confrontations: List<Confrontation> = emptyList(),
    val hasFinished: Boolean = false,
    val selected: Int?  = null,
) : FetchedData {

    /*
    Operators for UI.
     */
    val detail: Confrontation? get() = selected?.let { confrontations.getOrNull(selected) }
    val progress: Float get() = (selected!! + 1) / confrontations.size.toFloat()
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
    data class GetNext(val input: ConfrontationInput) : ConfrontationUiEvent()
}

@Parcelize
data class ConfrontationInput(
    val winner: Duel.Selection? = null,
    val score: Duo<Int>? = null,
) : Parcelable {
    val scoresVisible: Boolean get() = winner != null

    fun selectWinner(newSelected: Duel.Selection?): ConfrontationInput {
        val score = if (newSelected == winner) {
            score
        } else {
            null
        }
        return copy(winner = newSelected, score = score)
    }

    fun toAnswers(
        root: Node<Contest>
    ): AnswerAggregation {
        checkNotNull(winner)
        val winnerAnswer = (root.current as MatchWinner).select(winner)
        val path = root.paths[winnerAnswer.selected.pathIndex].current as IntScores
        val scoreAnswer = score?.let { score ->
            path.select(score)
        }
        return WinnerAnswers(
            winner = winnerAnswer,
            score = scoreAnswer
        )
    }
}

@KoinViewModel
class ConfrontationListViewModel(
    subscription: Subscription,
    private val placeGuessUseCase: PlaceGuessUseCase,
    private val repository: CompetitionRepository,
    private val savedState: SavedStateHandle
) : StateViewModel<ConfrontationData, ConfrontationUiEvent>(ConfrontationData(subscription)){

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
            is ConfrontationUiEvent.GetNext -> getNext(uiEvent.input)
        }
    }

    private fun tryAgain(message: SimpleMessage) {
        messageShown(message, loading = true)
        load()
    }

    private fun load() {
        setAsync {
            val confrontations = repository.getConfrontationsBy(it.data.subscription.competition.id)
            logger.d("fetch ${confrontations.size} confrontations")
            it.data(
                ConfrontationData.confrontations.set(it.data, confrontations),
            )
        }
    }

    private fun getNext(confrontationInput: ConfrontationInput) {
        setData { current ->
            if (confrontationInput.winner != null) {
                viewModelScope.launch {
                    val answers = confrontationInput.toAnswers(current.detail!!.contest)
                    val guess = Guess(
                        subscriptionId = current.subscription.id,
                        aggregation = answers,
                        confrontation = ConfrontationForGuess(
                            confrontation = current.detail!!,
                            competitionId = current.subscription.competition.id
                        )
                    )
                    placeGuessUseCase(guess)
                }
            }
            if (current.hasNext) {
                val selected = checkNotNull(current.selected) {
                    "If hasNext returns true so selected should be not null"
                }
                //ConfrontationData.selected.set(current, selected + 1)
            } else {
                //ConfrontationData.hasFinished.set(current, true)
            }
            current
        }
    }

    companion object {
        private const val SELECTED = "index"
    }
}