package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.collections.update
import br.com.mob1st.core.state.contracts.NavigationDelegate
import br.com.mob1st.core.state.contracts.NavigationManager
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
    SnackbarManager<CommonError> by SnackbarDelegate() {
    private val savedUserInput = builderStateSaver.getSavedValue {
        BuilderUserInput.fromUiState(uiState = uiStateOutput.value, suggestions = stateHolder.suggestions)
    }
    private val manuallyAddedState = MutableStateFlow(
        savedUserInput.getManuallyAdded(),
    )
    private val suggestedInputs = MutableSharedFlow<Pair<Int, String>>()

    private val errorHandler = ErrorHandler()
    private val manuallyAddedDebouncer = ValidationDebouncer(::validateItem)
    private val suggestionsDebouncer = ValidationDebouncer(::validateItem)

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
        entry: BuilderUserInput.Entry,
    ) = launchIn {
        manuallyAddedState.update { list ->
            list.update(position) { item ->
                item.copy(
                    name = entry.name,
                    amount = entry.amount,
                )
            }
        }
        manuallyAddedDebouncer.debounceValidation(
            ValidationRequest(position, entry.amount),
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
            .catchIn(errorHandler = errorHandler)
            .onEach { suggestions -> suggestionsDebouncer.create(suggestions.size) }
            .map { suggestions -> stateHolder.asUiState(suggestions, savedUserInput) }
            .combine(suggestedInputs) { list, (position, amount) ->
                list.update(position) { item ->
                    item.copy(amount = amount)
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

    private fun validateItem(position: Int, amount: String): ValidationResult {
        return try {
            amount.replace("[^\\d-]", "").toInt()
            ValidationResult(position, null)
        } catch (e: NumberFormatException) {
            errorHandler.catch(e)
            ValidationResult(position, ValidationResult.AmountError.InvalidFormat)
        }
    }
}
