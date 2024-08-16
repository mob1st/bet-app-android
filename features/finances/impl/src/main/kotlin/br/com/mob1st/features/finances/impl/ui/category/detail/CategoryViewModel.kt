package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.viewmodels.launchIn
import br.com.mob1st.core.kotlinx.checks.ifIs
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.coroutines.stateInWhileSubscribed
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.UiStateManager
import br.com.mob1st.core.state.managers.catching
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryDetailUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.SetCategoryUseCase
import br.com.mob1st.features.utils.errors.commonErrorHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber

/**
 * ViewModel for the CategoryDetail screen.
 */
internal class CategoryViewModel(
    private val default: DefaultCoroutineDispatcher,
    private val consumableDelegate: ConsumableDelegate<CategoryDetailConsumables>,
    private val categoryStateHandle: CategoryStateHandle,
    private val getCategoryDetail: GetCategoryDetailUseCase,
    private val setCategory: SetCategoryUseCase,
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
            .flatMapLatest { category ->
                categoryStateHandle.entry(category).map { entry ->
                    CategoryDetailUiState.Loaded(
                        category = category,
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
    fun type(number: Int) = errorHandler.catching {
        uiState.value.ifIs<CategoryDetailUiState.Loaded> { uiState ->
            val newEntry = uiState.appendNumber(number)
            categoryStateHandle.update(newEntry)
        }
    }

    /**
     * Displays the proper dialog for the current [CategoryDetailUiState.Loaded.category]
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     */
    fun openCalendar() = errorHandler.catching {
        uiState.value.ifIs<CategoryDetailUiState.Loaded> { uiState ->
            consumableDelegate.update {
                it.showDialog(uiState.category.recurrences)
            }
        }
    }

    /**
     * Deletes the last typed number in the [CategoryDetailUiState.Loaded].
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     */
    fun deleteNumber() = errorHandler.catching {
        uiState.value.ifIs<CategoryDetailUiState.Loaded> { state ->
            val newEntry = state.erase()
            categoryStateHandle.update(newEntry)
        }
    }

    fun decimal() = errorHandler.catching {
        Timber.d("TODO enable decimal typing")
    }

    /**
     * Reverts all changes done, restoring the initial state of the [CategoryDetailUiState.Loaded].
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     */
    fun undo() = errorHandler.catching {
        uiState.value.ifIs<CategoryDetailUiState.Loaded> {
            categoryStateHandle.update(it.undo())
        }
    }

    /**
     * Submits the current [CategoryDetailUiState.Loaded], writing the changes in the [CategoryStateHandle].
     * If the state is not [CategoryDetailUiState.Loaded] it does nothing.
     */
    fun submit() = launchIn(default + errorHandler) {
        uiState.value.ifIs<CategoryDetailUiState.Loaded> { state ->
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
