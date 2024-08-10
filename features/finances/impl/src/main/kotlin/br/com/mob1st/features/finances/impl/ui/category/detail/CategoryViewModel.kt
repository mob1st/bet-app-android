package br.com.mob1st.features.finances.impl.ui.category.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.viewmodels.launchIn
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.coroutines.stateInWhileSubscribed
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.core.state.managers.AsyncLoadingState
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.UiStateManager
import br.com.mob1st.core.state.managers.catching
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryDetailUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.SetCategoryUseCase
import br.com.mob1st.features.utils.errors.commonErrorHandler
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber

internal class CategoryViewModel(
    private val default: DefaultCoroutineDispatcher,
    private val consumableDelegate: ConsumableDelegate<CategoryDetailConsumables>,
    private val categoryStateHandle: CategoryStateHandle,
    intent: GetCategoryIntent,
    getCategoryDetail: GetCategoryDetailUseCase,
    private val setCategory: SetCategoryUseCase,
) : ViewModel(),
    UiStateManager<CategoryDetailState>,
    ConsumableManager<CategoryDetailConsumables> by consumableDelegate {
    override val uiState: StateFlow<CategoryDetailState> = getCategoryDetail[intent]
        .map { CategoryDetailState.Loaded(it) }
        .stateInWhileSubscribed(
            viewModelScope,
            CategoryDetailState.Loading,
        )

    private val errorHandler = consumableDelegate.commonErrorHandler {
        copy(commonErrorSnackbarState = it)
    }
    private val asyncLoadingState = AsyncLoadingState()

    init {
        Timber.d("ptest init viewmodel ${hashCode()}")
    }

    fun type(number: Int) = launchIn(errorHandler) {
    }

    fun openCalendar() = errorHandler.catching {
        val uiState = checkIs<CategoryDetailState.Loaded>(uiState.value)
        consumableDelegate.update {
            it.copy(
                dialog = CategoryCalendarDialog(
                    uiState.category.recurrences,
                ),
            )
        }
    }

    fun submitCalendar() {
        Timber.d("ptest submit calendar")
    }

    fun deleteNumber() {
        Timber.d("ptest delete number")
    }

    fun deleteCategory() {
        Timber.d("ptest delete category")
    }

    fun decimal() = errorHandler.catching {
    }

    fun undo() = errorHandler.catching {
    }

    fun submit() = launchIn(default + errorHandler) {
        val uiState = uiState.value
        if (uiState !is CategoryDetailState.Loaded) {
            return@launchIn
        }
        asyncLoadingState.trigger {
            setCategory(uiState.category)
        }
    }

    companion object {
        fun consumableDelegate() = ConsumableDelegate(
            CategoryDetailConsumables(),
        )
    }
}
