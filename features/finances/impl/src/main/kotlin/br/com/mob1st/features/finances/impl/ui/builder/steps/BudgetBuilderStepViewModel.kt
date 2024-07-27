package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInWhileSubscribed
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.errors.checkIs
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.core.state.contracts.UiStateOutputManager
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.catchIn
import br.com.mob1st.core.state.managers.catching
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.NotEnoughInputsException
import br.com.mob1st.features.finances.impl.domain.events.NotEnoughItemsToCompleteEvent
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryBuilderUseCase
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepUiState.Empty
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepUiState.Loaded
import br.com.mob1st.features.utils.states.errorHandler
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class BudgetBuilderStepViewModel private constructor(
    step: BuilderNextAction.Step,
    getCategoryBuilder: GetCategoryBuilderUseCase,
    private val analyticsReporter: AnalyticsReporter,
    default: DefaultCoroutineDispatcher,
    private val consumableDelegate: ConsumableDelegate<BudgetBuilderStepConsumables>,
) : ViewModel(),
    UiStateOutputManager<BudgetBuilderStepUiState>,
    ConsumableManager<BudgetBuilderStepConsumables> by consumableDelegate {
    constructor(
        step: BuilderNextAction.Step,
        getCategoryBuilder: GetCategoryBuilderUseCase,
        analyticsReporter: AnalyticsReporter,
        default: DefaultCoroutineDispatcher,
    ) : this(
        step,
        getCategoryBuilder,
        analyticsReporter,
        default,
        ConsumableDelegate(
            BudgetBuilderStepConsumables(),
        ),
    )

    private val errorHandler = consumableDelegate.errorHandler {
        handleError(it)
    }

    override val consumableUiState: StateFlow<BudgetBuilderStepConsumables> = consumableDelegate.asStateFlow()
    override val uiStateOutput: StateFlow<BudgetBuilderStepUiState> = getCategoryBuilder[step]
        .map(::Loaded)
        .catchIn(errorHandler)
        .flowOn(default)
        .stateInWhileSubscribed(viewModelScope, Empty)

    /**
     * Selects the manually added item at the given [position].
     * @param position The position of the selected item.
     */
    fun selectManuallyAddedItem(position: Int) = errorHandler.catching {
        val uiState = checkIs<Loaded>(uiStateOutput.value)
        consumableDelegate.update {
            it.selectItem(uiState.manuallyAdded[position])
        }
    }

    /**
     * Selects the suggested item at the given [position].
     * @param position The position of the selected item.
     */
    fun selectSuggestedItem(position: Int) = errorHandler.catching {
        val uiState = checkIs<Loaded>(uiStateOutput.value)
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
    fun next() = errorHandler.catching {
        val uiState = checkIs<Loaded>(uiStateOutput.value)
        try {
            val result = uiState.builder.next()
            consumableDelegate.update {
                it.navigateToNext(result)
            }
        } catch (e: NotEnoughInputsException) {
            // avoid logging this specific exception as an error
            analyticsReporter.log(NotEnoughItemsToCompleteEvent(uiState.builder.id, e.remainingInputs))
            consumableDelegate.update {
                it.handleError(e)
            }
        }
    }
}
