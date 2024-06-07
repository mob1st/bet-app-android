package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.state.contracts.NavigationDelegate
import br.com.mob1st.core.state.contracts.NavigationManager
import br.com.mob1st.core.state.managers.DialogDelegate
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.SnackbarDelegate
import br.com.mob1st.core.state.managers.SnackbarManager
import br.com.mob1st.core.state.managers.catchIn
import br.com.mob1st.core.state.managers.launchIn
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.GetSuggestionsUseCase
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.SetCategoryBatchUseCase
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.dialogErrorHandler
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

internal class BuilderViewModel(
    getSuggestionsUseCase: GetSuggestionsUseCase,
    private val setCategoryBatchUseCase: SetCategoryBatchUseCase,
    private val builderUserStateFlow: BuilderUserStateFlow,
    private val stateHolder: BuilderUiStateHolder,
) : ViewModel(),
    BuilderUiStateManager.Input,
    BuilderUiStateManager.Output,
    NavigationManager<Unit> by NavigationDelegate(),
    SnackbarManager<CommonError> by SnackbarDelegate(),
    DialogManager<CommonError> by DialogDelegate() {
    private val inputErrorState = MutableStateFlow(persistentListOf<Int>())

    override val uiStateOutput: StateFlow<BuilderUiState> = getSuggestionsUseCase[stateHolder.recurrenceType]
        .catchIn(dialogErrorHandler)
        .combine(builderUserStateFlow, stateHolder::asUiState)
        .stateInRetained(viewModelScope, BuilderUiState())

    override fun addManualCategory() =
        builderUserStateFlow.update { inputState ->
            inputState + BuilderUserInput.Entry()
        }

    override fun updateManualCategory(
        position: Int,
        name: String,
        value: String,
    ) = builderUserStateFlow.update { inputState ->
        // atualiza na tela o text imediatamente na posicao especificada
        // aplica um debounce para atualizar o resultado da validacao
        // envia usando shared flow o resutado da valicadao pra ser apresentada na UI
        // salva os inputs na tela convertendo para Parcelable apenas no ON SAVE

        inputState.update(position) { entry ->
            entry.copy(name = name, amount = value)
        }
    }

    override fun updateSuggestedCategory(
        position: Int,
        value: String,
    ) = builderUserStateFlow.update { inputState ->
        inputState.update(stateHolder.getSuggestionId(position)) { entry ->
            entry.copy(amount = value)
        }
    }

    override fun save() =
        launchIn {
            val batch = stateHolder.createBatch()
            setCategoryBatchUseCase(batch)
        }
}
