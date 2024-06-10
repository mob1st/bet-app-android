package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.state.contracts.NavigationDelegate
import br.com.mob1st.core.state.contracts.NavigationManager
import br.com.mob1st.core.state.managers.DialogDelegate
import br.com.mob1st.core.state.managers.DialogManager
import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.SheetManager
import br.com.mob1st.core.state.managers.SnackbarDelegate
import br.com.mob1st.core.state.managers.SnackbarManager
import br.com.mob1st.core.state.managers.catchIn
import br.com.mob1st.core.state.managers.launchIn
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.GetSuggestionsUseCase
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.SetCategoryBatchUseCase
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.features.utils.errors.snackbarErrorHandler
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
internal class BuilderViewModel(
    builderStateSaver: BuilderStateSaver,
    private val getSuggestionsUseCase: GetSuggestionsUseCase,
    private val setCategoryBatchUseCase: SetCategoryBatchUseCase,
    private val stateHolder: BuilderUiStateHolder,
    private val categorySheetDelegate: CategorySheetDelegate,
) : ViewModel(),
    BuilderUiStateManager.Input,
    BuilderUiStateManager.Output,
    DialogManager<CategoryNameDialog> by DialogDelegate(),
    NavigationManager<Unit> by NavigationDelegate(),
    SnackbarManager<CommonError> by SnackbarDelegate(),
    SheetManager<CategorySheet> by categorySheetDelegate {
    private val savedUserInput = builderStateSaver.getSavedValue {
        uiStateOutput.value.toSavedState(stateHolder.suggestions)
    }
    private val categoryNameDialogState = MutableStateFlow<CategoryNameDialog?>(null)

    override val uiStateOutput: StateFlow<BuilderUiState> = combine(
        getManualAddedItems(),
        getSuggestionItems(),
        categoryNameDialogState,
        ::BuilderUiState,
    ).stateInRetained(viewModelScope, BuilderUiState())

    override fun selectManualCategory(position: Int) = uiStateOutput.value.run {
        if (isAddButton(position)) {
            categoryNameDialogState.value = CategoryNameDialog()
        } else {
            categorySheetDelegate.showSheet(showUpdateManualSheet(position))
        }
    }

    override fun selectSuggestedCategory(position: Int) {
        categorySheetDelegate.showSheet(uiStateOutput.value.showUpdateSuggestedSheet(position))
    }

    override fun typeManualCategoryName(name: String) = launchIn(snackbarErrorHandler) {
        categoryNameDialogState.update { dialog ->
            checkNotNull(dialog).copy(
                text = name,
                isSubmitEnabled = name.isNotBlank() && name.length >= 2,
            )
        }
    }

    override fun submitManualCategoryName() = launchIn(snackbarErrorHandler) {
        val dialog = checkNotNull(categoryNameDialogState.getAndUpdate { null })
        categorySheetDelegate.showSheet(CategorySheet.add(name = dialog.text))
    }

    override fun updateCategory() = launchIn(snackbarErrorHandler) {
        categorySheetDelegate.updateCategory()
    }

    private fun getManualAddedItems(): StateFlow<PersistentList<BuilderUiState.ListItem>> {
        val initialValue = savedUserInput.manuallyAdded.toManualListItem()
        val manuallyAddedItems = MutableStateFlow(initialValue.toPersistentList())
        categorySheetDelegate.manualItemUpdateInput
            .onEach { update ->
                manuallyAddedItems.update { items -> items.applyUpdate(update) }
            }
            .launchIn(viewModelScope)
        return manuallyAddedItems.asStateFlow()
    }

    private fun getSuggestionItems() = getSuggestionsUseCase[stateHolder.recurrenceType]
        .map { suggestions ->
            stateHolder
                .suggestionAsState(suggestions, savedUserInput)
                .toPersistentList()
        }
        .transformLatest { items ->
            emit(items)
            val updatedItems = categorySheetDelegate
                .suggestedItemUpdateInput
                .map { update -> items.applyUpdate(update) }
            emitAll(updatedItems)
        }
        .catchIn(ErrorHandler())

    override fun save() = launchIn(snackbarErrorHandler) {
        val batch = stateHolder.createBatch()
        setCategoryBatchUseCase(batch)
    }
}
