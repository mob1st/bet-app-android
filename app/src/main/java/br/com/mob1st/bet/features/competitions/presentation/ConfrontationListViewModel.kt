package br.com.mob1st.bet.features.competitions.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.competitions.domain.Confrontation
import br.com.mob1st.bet.features.competitions.domain.GetConfrontationListUseCase
import org.koin.android.annotation.KoinViewModel
import org.koin.androidx.compose.koinViewModel
import javax.annotation.concurrent.Immutable
// any use case class to map the user actions and call the API
class CreateGuessUseCase() {
    suspend operator fun invoke(){
        // TODO call repository
    }
}

@Immutable
data class ConfrontationData(
    val confrontations: List<Confrontation> = emptyList(),
    val customErrorVisible: Boolean = false
)

sealed class ConfrontationUiEvent {
    data class CreateGuess(
        // this property is just an example to show how to pass parameters from UI to the ViewModel
        val team1Winner: Boolean,
    ) : ConfrontationUiEvent()
    object TryAgain : ConfrontationUiEvent()
}

@KoinViewModel
class ConfrontationListViewModel(
    private val getConfrontationListUseCase: GetConfrontationListUseCase,
    private val createGuessUseCase: CreateGuessUseCase,
) : StateViewModel<ConfrontationData, ConfrontationUiEvent>(
    AsyncState(data = ConfrontationData(), loading = true)
) {

    init {
        setState {
            // load the data when the viewmodel starts
            it.data(data = it.data.copy(confrontations = getConfrontationListUseCase()))
        }
    }

    override fun fromUi(uiEvent: ConfrontationUiEvent) {
        when (uiEvent) {
            is ConfrontationUiEvent.CreateGuess -> callCreateGuess()
            ConfrontationUiEvent.TryAgain -> TODO()
        }
    }

    private fun callCreateGuess() {
        setState {
            it.loading()
        }
        setState { current: AsyncState<ConfrontationData> ->
            // call current.data() function and update the data
            current

        }
    }

    private fun getConfrontationList() {
        setState {
            it.loading()
        }
        setState {
            it.data(data = it.data.copy(confrontations = getConfrontationListUseCase()))
        }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ExampleOfUi() {
    val viewModel = koinViewModel<ConfrontationListViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Column() {
        if (state.loading) {
            // Show some loading state
        }

        if (state.messages.isNotEmpty()) {
            // show the error message
            Button(onClick = {
                viewModel.fromUi(ConfrontationUiEvent.TryAgain)
            }) {
                Text(text = "try again")
            }
        }

        Button(onClick = { viewModel.fromUi(ConfrontationUiEvent.CreateGuess(team1Winner = false)) }) {
            Text(text = "Create Guess")
        }

    }
}