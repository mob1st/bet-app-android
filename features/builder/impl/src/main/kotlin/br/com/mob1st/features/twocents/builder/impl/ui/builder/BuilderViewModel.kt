package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.collections.update
import br.com.mob1st.core.state.contracts.NavigationDelegate
import br.com.mob1st.core.state.contracts.NavigationManager
import br.com.mob1st.core.state.managers.DialogDelegate
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.SnackbarDelegate
import br.com.mob1st.core.state.managers.SnackbarManager
import br.com.mob1st.core.state.managers.catchIn
import br.com.mob1st.core.state.managers.launchIn
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.GetSuggestionsUseCase
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.SetCategoryBatchUseCase
import br.com.mob1st.features.utils.errors.CommonError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
internal class BuilderViewModel(
    builderStateSaver: BuilderStateSaver,
    private val getSuggestionsUseCase: GetSuggestionsUseCase,
    private val setCategoryBatchUseCase: SetCategoryBatchUseCase,
    private val stateHolder: BuilderUiStateHolder,
) : ViewModel(),
    BuilderUiStateManager.Input,
    BuilderUiStateManager.Output,
    NavigationManager<Unit> by NavigationDelegate(),
    SnackbarManager<CommonError> by SnackbarDelegate(),
    DialogManager<CommonError> by DialogDelegate() {
    private val savedUserInput = builderStateSaver.getSavedValue {
        BuilderUserInput.fromUiState(uiState = uiStateOutput.value, suggestions = stateHolder.suggestions)
    }

    private val manuallyAddedState = MutableStateFlow(
        savedUserInput.manuallyAdded.map {
            BuilderUiState.ListItem(
                name = it.name,
                amount = InputState(it.amount),
            )
        }.toPersistentList(),
    )

    private val suggestedInputs = MutableSharedFlow<Pair<Int, String>>()

    private val manuallyAddedDebouncer = ValidationDebouncer(ErrorHandler())
    private val suggestionsDebouncer = ValidationDebouncer(ErrorHandler())

    override val uiStateOutput: StateFlow<BuilderUiState> = combine(
        getManuallyAddedItems(),
        getSuggestionItems(),
        ::BuilderUiState,
    ).stateInRetained(viewModelScope, BuilderUiState())

    override fun addManualCategory() {
        manuallyAddedState.update {
            it + BuilderUiState.ListItem(name = "")
        }
    }

    override fun updateManualCategory(
        position: Int,
        name: String,
        amount: String,
    ) = launchIn {
        manuallyAddedState.update { list ->
            list.update(position) { item ->
                item.copy(
                    name = name,
                    amount = InputState(amount),
                )
            }
        }
        manuallyAddedDebouncer.debounceValidation(
            ValidationRequest(position, amount),
        )
    }

    override fun updateSuggestedCategory(
        position: Int,
        amount: String,
    ) = launchIn {
        suggestedInputs.emit(position to amount)
        suggestionsDebouncer.debounceValidation(
            ValidationRequest(position, amount),
        )
    }

    private fun getSuggestionItems(): Flow<ImmutableList<BuilderUiState.ListItem<Int>>> {
        return getSuggestionsUseCase[stateHolder.recurrenceType]
            .catchIn(errorHandler = ErrorHandler())
            .onEach { suggestions -> suggestionsDebouncer.create(suggestions.size) }
            .map { suggestions -> stateHolder.asUiState(suggestions, savedUserInput) }
            .combine(suggestedInputs) { list, (position, amount) ->
                list.update(position) { item ->
                    item.copy(amount = InputState(amount))
                }
            }
            .flatMapLatest(suggestionsDebouncer::results)
    }

    private fun getManuallyAddedItems(): Flow<ImmutableList<BuilderUiState.ListItem<String>>> {
        return manuallyAddedState
            .onEach { manuallyAddedDebouncer.create(it.size) }
            .flatMapLatest(manuallyAddedDebouncer::results)
    }

    override fun save() = launchIn {
        val batch = stateHolder.createBatch()
        setCategoryBatchUseCase(batch)
    }
}
