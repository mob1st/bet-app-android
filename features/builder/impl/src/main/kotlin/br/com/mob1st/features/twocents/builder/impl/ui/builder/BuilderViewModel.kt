package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.state.contracts.NavigationDelegate
import br.com.mob1st.core.state.contracts.NavigationManager
import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.SnackbarManager
import br.com.mob1st.core.state.managers.catchIn
import br.com.mob1st.core.state.managers.launchIn
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.GetSuggestionsUseCase
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.SetCategoryBatchUseCase
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.SnackbarErrorHandler
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
internal class BuilderViewModel(
    initialState: BuilderUiState,
    builderStateSaver: BuilderStateSaver,
    private val getSuggestionsUseCase: GetSuggestionsUseCase,
    private val setCategoryBatchUseCase: SetCategoryBatchUseCase,
    private val sideEffects: SideEffects = SideEffects(),
) : ViewModel(),
    BuilderUiStateManager,
    CategorySheetManager by sideEffects.categorySheetDelegate,
    CategoryNameDialogManager by sideEffects.categoryNameDialogDelegate,
    SnackbarManager<CommonError> by sideEffects.snackbarErrorHandler,
    NavigationManager<Unit> by sideEffects.navigationDelegate {
    private val savingLoaderState = MutableStateFlow(false)

    private val savedUserInput = builderStateSaver.getSavedValue {
        uiStateOutput.value.toSavedState()
    }

    override val uiStateOutput: StateFlow<BuilderUiState> = combine(
        getManualAddedItems(initialState),
        getSuggestionItems(initialState),
        savingLoaderState,
        initialState::combine,
    ).stateInRetained(viewModelScope, initialState)

    override fun selectManualCategory(position: Int) = with(uiStateOutput.value) {
        showSheet(showUpdateManualSheet(position))
    }

    override fun selectSuggestedCategory(position: Int) = with(uiStateOutput.value) {
        showSheet(showUpdateSuggestedSheet(position))
    }

    override fun submitManualCategoryName() = launchIn(sideEffects.snackbarErrorHandler) {
        val text = sideEffects.categoryNameDialogDelegate.getNameAndSubmit()
        showSheet(uiStateOutput.value.showNewManualSheet(text))
    }

    /**
     * Provides a mutable list of manually added items that add/set items depending on the user input.
     * The initial state is a combination of the [initialState] and the [savedUserInput] in case the screen is restored.
     * @param initialState The initial state of the UI.
     * @return The UI state for the manual items, updating its values with the user input.
     */
    private fun getManualAddedItems(
        initialState: BuilderUiState,
    ): StateFlow<ManualCategoryBuilderSection> = with(sideEffects.categorySheetDelegate) {
        val initialValue = initialState.createManualSuggestionsSection(savedUserInput)
        val manuallyAddedItems = MutableStateFlow(initialValue)
        manualItemUpdateInput
            .onEach { update ->
                manuallyAddedItems.update { section -> section.applyUpdate(update) }
            }
            .launchIn(viewModelScope)
        manuallyAddedItems.asStateFlow()
    }

    /**
     * Combines the suggestions provided from domain layer with the updates from the UI.
     * The initial state is a combination of the [initialState] and the [savedUserInput] in case the screen is restored.
     * @param initialState The initial state of the UI.
     * @return The UI state for the suggestions, updating its values with the user input.
     */
    private fun getSuggestionItems(
        initialState: BuilderUiState,
    ): Flow<SuggestedCategoryBuilderSection> = with(sideEffects.categorySheetDelegate) {
        getSuggestionsUseCase[initialState.categoryType]
            .map { suggestions ->
                initialState.createSuggestedSection(
                    suggestions = suggestions.toImmutableList(),
                    userInput = savedUserInput,
                )
            }
            .transformLatest { section ->
                emit(section)
                val updatedItems = suggestedItemUpdateInput.map(section::applyUpdate)
                emitAll(updatedItems)
            }
            .catchIn(ErrorHandler())
    }

    override fun save() = launchIn(sideEffects.snackbarErrorHandler) {
        try {
            savingLoaderState.value = true
            setCategoryBatchUseCase(uiStateOutput.value.toBatch())
        } finally {
            savingLoaderState.value = false
        }
    }

    /**
     * Compiles all side effects produced by the ViewModel in a single place, avoiding multiple dependencies in the
     * ViewModel's constructor.
     */
    class SideEffects(
        val navigationDelegate: NavigationDelegate<Unit> = NavigationDelegate(),
        val snackbarErrorHandler: SnackbarErrorHandler = SnackbarErrorHandler(),
        val categorySheetDelegate: CategorySheetDelegate = CategorySheetDelegate(snackbarErrorHandler),
        val categoryNameDialogDelegate: CategoryNameDialogDelegate = CategoryNameDialogDelegate(snackbarErrorHandler),
    )
}
