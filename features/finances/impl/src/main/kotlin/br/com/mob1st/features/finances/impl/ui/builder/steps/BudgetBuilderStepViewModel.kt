package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.viewmodels.launchIn
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.coroutines.stateInWhileSubscribed
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.core.state.extensions.errorHandler
import br.com.mob1st.core.state.managers.AsyncLoadingState
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.UiStateManager
import br.com.mob1st.core.state.managers.catchIn
import br.com.mob1st.core.state.managers.catching
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.usecases.GetBudgetBuilderForStepUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderUseCase
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepUiState.Loaded
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update

internal class BudgetBuilderStepViewModel(
    private val default: DefaultCoroutineDispatcher,
    private val consumableDelegate: ConsumableDelegate<BudgetBuilderStepConsumables>,
    private val step: BuilderNextAction.Step,
    private val getCategoryBuilder: GetBudgetBuilderForStepUseCase,
    private val proceedBuilder: ProceedBuilderUseCase,
) : ViewModel(),
    UiStateManager<BudgetBuilderStepUiState>,
    ConsumableManager<BudgetBuilderStepConsumables> by consumableDelegate {
    private val errorHandler = consumableDelegate.errorHandler {
        handleError(it)
    }
    private val isLoadingState = AsyncLoadingState()

    override val consumableUiState: StateFlow<BudgetBuilderStepConsumables> = consumableDelegate.asStateFlow()
    override val uiState: StateFlow<Loaded> =
        initState()
            .catchIn(errorHandler)
            .flowOn(default)
            .stateInWhileSubscribed(viewModelScope, Loaded(step))

    private fun initState() = combine(
        getCategoryBuilder[step],
        isLoadingState,
        transform = ::Loaded,
    )

    /**
     * Selects the manually added item at the given [position].
     * @param position The position of the selected item.
     */
    fun selectManuallyAddedItem(position: Int) = errorHandler.catching {
        val uiState = checkIs<Loaded>(uiState.value)
        consumableDelegate.update {
            it.selectItem(uiState.manuallyAdded[position])
        }
    }

    /**
     * Selects the suggested item at the given [position].
     * @param position The position of the selected item.
     */
    fun selectSuggestedItem(position: Int) = errorHandler.catching {
        val uiState = checkIs<Loaded>(uiState.value)
        consumableDelegate.update {
            it.selectItem(uiState.suggestions[position])
        }
    }

    /**
     * Starts the steps to create a new manually added category.
     */
    fun addNewCategory() = errorHandler.catching {
        consumableDelegate.update {
            it.showCategoryNameDialog()
        }
    }

    /**
     * Adds the given [name] to the manually added categories.
     * @param name The name of the category.
     */
    fun typeCategoryName(name: String) {
        consumableDelegate.update {
            it.typeCategoryName(name)
        }
    }

    /**
     * Submits the category name to be added to the manually added categories.
     */
    fun submitCategoryName() = errorHandler.catching {
        consumableDelegate.update {
            it.submitCategoryName()
        }
    }

    /**
     * Proceeds to the next step of the builder.
     */
    fun next() = launchIn(default + errorHandler) {
        val uiState = checkIs<Loaded>(uiState.value)
        isLoadingState.trigger {
            proceedBuilder(uiState.builder)
        }
        consumableDelegate.update {
            it.copy(action = uiState.builder.next)
        }
    }

    companion object {
        /**
         * Creates a new [ConsumableDelegate] for the [BudgetBuilderStepViewModel].
         */
        fun consumableDelegate() = ConsumableDelegate(BudgetBuilderStepConsumables())
    }
}
