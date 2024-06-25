package br.com.mob1st.features.twocents.builder.impl.ui.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.Lens
import arrow.optics.copy
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.launchIn
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import br.com.mob1st.features.twocents.builder.impl.domain.entities.CategoryInput
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.GetSuggestionsUseCase
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.SetCategoryBatchUseCase
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
internal class BuilderViewModel2(
    categoryType: CategoryType,
    private val getSuggestions: GetSuggestionsUseCase,
    private val setCategoryBatch: SetCategoryBatchUseCase,
) : ViewModel(), ConsumableManager<BuilderConsumables> {
    // inputs
    private val suggestedInputs = MutableSharedFlow<BuilderBottomSheet.CategorySheet>()

    // states
    private val builderUiState = MutableStateFlow(BuilderUiState(categoryType))
    private val consumablesState = MutableStateFlow(BuilderConsumables())
    private val manualAddedItemsState = MutableStateFlow<PersistentList<BuilderListItem2>>(persistentListOf())

    val uiOutput2 = combine(
        manualAddedItemsState,
        getCategoriesInputList(categoryType),
        consumablesState,
    ) { manualAddedItems, suggestions, consumables ->
        BuilderUiState2(
            categoryType = categoryType,
            manuallyAddedCategories = manualAddedItems,
            suggestedCategories = suggestions,
            consumable = consumables,
            isSaving = consumables.isSaving,
        )
    }.stateInRetained(
        scope = viewModelScope,
        initialValue = BuilderUiState2(categoryType),
    )

    private fun getCategoriesInputList(categoryType: CategoryType): Flow<PersistentList<BuilderListItem2>> {
        return getSuggestions[categoryType]
            .take(1)
            .map { suggestions ->
                suggestions.map { suggestion ->
                    BuilderListItem2(
                        input = CategoryInput(
                            type = categoryType,
                            linkedSuggestion = suggestion,
                        ),
                    )
                }.toPersistentList()
            }
            .flatMapLatest { suggestions ->
                suggestedInputs.runningFold(suggestions) { list, sheet ->
                    list.set(checkNotNull(sheet.position), BuilderListItem2(sheet.input))
                }
            }
    }

    fun showCategoryNameDialog() {
        val categoryType = uiOutput2.value.categoryType
        consumablesState.update {
            it.copy(
                bottomSheet = BuilderBottomSheet.CategorySheet(
                    input = CategoryInput(type = categoryType),
                    position = null,
                ),
            )
        }
    }

    fun setCategoryName(name: String) {
        consumablesState.update {
            BuilderConsumables.dialog.set(it, BuilderDialog.CategoryNameDialog(name))
        }
    }

    fun submitCategoryName() {
        val categoryType = uiOutput2.value.categoryType
        consumablesState.update {
            val dialog = checkIs<BuilderDialog.CategoryNameDialog>(it.dialog)
            it.copy(
                bottomSheet = BuilderBottomSheet.CategorySheet(
                    input = CategoryInput(
                        type = categoryType,
                        name = dialog.name,
                    ),
                    position = null,
                ),
                dialog = null,
            )
        }
    }

    fun selectSuggestedCategory(position: Int) {
        val suggestedCategory = uiOutput2.value.suggestedCategories[position]
        consumablesState.update {
            it.copy(
                bottomSheet = BuilderBottomSheet.CategorySheet(
                    input = suggestedCategory.input,
                    position = position,
                ),
            )
        }
    }

    fun selectManuallyAddedCategory(position: Int) {
        val manualCategory = uiOutput2.value.manuallyAddedCategories[position]
        consumablesState.update {
            it.copy(
                bottomSheet = BuilderBottomSheet.CategorySheet(
                    input = manualCategory.input,
                    position = position,
                ),
            )
        }
    }

    fun submitCategory() = launchIn {
        val consumables = consumablesState.getAndUpdate { it.copy(bottomSheet = null) }
        val sheet = checkIs<BuilderBottomSheet.CategorySheet>(consumables.bottomSheet)
        val input = sheet.input
        if (input.linkedSuggestion != null && sheet.position != null) {
            suggestedInputs.emit(sheet)
        } else {
            if (sheet.position != null) {
                manualAddedItemsState.update {
                    it.set(sheet.position, BuilderListItem2(input))
                }
            } else {
                manualAddedItemsState.update {
                    it + BuilderListItem2(input)
                }
            }
        }
    }

    fun save() = launchIn {
        try {
            consumablesState.update { it.copy(isSaving = true) }
            setCategoryBatch(uiOutput2.value.toBatch())
            consumablesState.update {
                it.copy(isSaving = false, navTarget = BuilderNavTarget.Next)
            }
        } finally {
            consumablesState.update { it.copy(isSaving = false) }
        }
    }

    override fun <Property> consume(lens: Lens<BuilderConsumables, Property?>) = consumablesState.update {
        it.copy { lens set null }
    }
}
