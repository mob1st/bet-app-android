package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.viewmodels.launchIn
import br.com.mob1st.core.kotlinx.checks.checkIs
import br.com.mob1st.core.kotlinx.checks.ifIs
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.coroutines.stateInWhileSubscribed
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.UiStateManager
import br.com.mob1st.core.state.managers.catching
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryDetailUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.SetCalculatorPreferencesUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.SetCategoryUseCase
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailUiState.Loaded
import br.com.mob1st.features.utils.errors.commonErrorHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the CategoryDetail screen.
 */
internal class CategoryViewModel(
    private val default: DefaultCoroutineDispatcher,
    private val consumableDelegate: ConsumableDelegate<CategoryDetailConsumables>,
    private val categoryStateHandle: CategoryStateHandle,
    private val getCategoryDetail: GetCategoryDetailUseCase,
    private val setCategory: SetCategoryUseCase,
    private val setPreferences: SetCalculatorPreferencesUseCase,
    args: CategoryDetailArgs,
) : ViewModel(),
    UiStateManager<CategoryDetailUiState>,
    ConsumableManager<CategoryDetailConsumables> by consumableDelegate {
    private val errorHandler = consumableDelegate.commonErrorHandler {
        copy(snackbarState = it)
    }

    override val uiState: StateFlow<CategoryDetailUiState> =
        get(args)
            .stateInWhileSubscribed(
                viewModelScope,
                CategoryDetailUiState.Loading,
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun get(args: CategoryDetailArgs): Flow<CategoryDetailUiState> {
        return getCategoryDetail[args.toIntent()]
            .flatMapLatest { categoryDetail ->
                categoryStateHandle.entry(categoryDetail.category).map { entry ->
                    Loaded(
                        detail = categoryDetail,
                        entry = entry,
                    )
                }
            }
    }

    /**
     * Types the given [number] in the [CategoryDetailUiState.Loaded].
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     * @param number The number to type.
     */
    fun type(number: Int) = ifIs<Loaded>(uiState.value) { uiState ->
        val newEntry = uiState.appendNumber(number)
        categoryStateHandle.update(newEntry)
    }

    /**
     * Displays the proper dialog for the current recurrences in the [CategoryDetailUiState.Loaded.detail].
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     */
    fun openCalendarDialog() = ifIs<Loaded>(uiState.value) { uiState ->
        consumableDelegate.update {
            it.showRecurrencesDialog(uiState.detail.category.recurrences)
        }
    }

    /**
     * Deletes the last typed number in the [CategoryDetailUiState.Loaded].
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     */
    fun deleteNumber() = ifIs<Loaded>(uiState.value) { state ->
        val newEntry = state.erase()
        categoryStateHandle.update(newEntry)
    }

    /**
     * Toggles the decimal mode of the amount in the [CategoryDetailUiState.Loaded].
     */
    fun toggleDecimal() = launchIn(default + errorHandler) {
        ifIs<Loaded>(uiState.value) { uiState ->
            val newState = uiState.toggleDecimalMode()
            categoryStateHandle.update(newState.entry)
            setPreferences(newState.detail.preferences)
        }
    }

    /**
     * Reverts all changes done, restoring the initial state of the [CategoryDetailUiState.Loaded].
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     */
    fun openIconPickerDialog() = ifIs<Loaded>(uiState.value) { uiState ->
        consumableDelegate.update { consumables ->
            consumables.showIconPickerDialog(uiState.entry.image)
        }
    }

    /**
     * Opens the dialog to allow the user to type a new name for the category.
     */
    fun openEnterName() = ifIs<Loaded>(uiState.value) { uiState ->
        consumableDelegate.update {
            it.showEnterCategoryNameDialog(uiState.entry.name)
        }
    }

    /**
     * Updates the enter cagegory name dialog input text
     */
    fun setCategoryName(name: String) = errorHandler.catching {
        consumableDelegate.update {
            it.setName(name)
        }
    }

    /**
     * Opens the dialog to allow the user to type a new name for the category.
     */
    fun openEditRecurrencesDialog() = ifIs<Loaded>(uiState.value) { uiState ->
        consumableDelegate.update { consumableDelegate ->
            consumableDelegate.showRecurrencesDialog(
                uiState.detail.category.recurrences,
            )
        }
    }

    /**
     * Submits the current dialog in the [CategoryDetailUiState.Loaded].
     */
    fun submitDialog() = errorHandler.catching {
        val uiState = checkIs<Loaded>(uiState.value)
        val dialog = checkNotNull(consumableDelegate.value.dialog)
        val newEntry = uiState.submitDialog(dialog)
        categoryStateHandle.update(newEntry)
    }

    /**
     * Submits the current [CategoryDetailUiState.Loaded], writing the changes in the [CategoryStateHandle].
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     */
    fun submit() = launchIn(default + errorHandler) {
        ifIs<Loaded>(uiState.value) { state ->
            setCategory(state.merge())
            consumableDelegate.update {
                it.copy(isSubmitted = true)
            }
        }
    }

    companion object {
        /**
         * Creates a delegate for the [CategoryViewModel].
         */
        fun consumableDelegate() = ConsumableDelegate(
            CategoryDetailConsumables(),
        )
    }
}
