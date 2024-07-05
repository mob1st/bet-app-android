package br.com.mob1st.features.finances.impl.ui.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
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
import br.com.mob1st.features.utils.states.errorHandler
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class CategoryBuilderViewModel(
    step: BuilderNextAction.Step,
    getCategoryBuilder: GetCategoryBuilderUseCase,
    private val analyticsReporter: AnalyticsReporter,
    default: DefaultCoroutineDispatcher,
    private val consumableDelegate: ConsumableDelegate<CategoryBuilderConsumables> = ConsumableDelegate(
        CategoryBuilderConsumables(),
    ),
) : ViewModel(),
    UiStateOutputManager<CategoryBuilderUiState>,
    ConsumableManager<CategoryBuilderConsumables> by consumableDelegate {
    private val errorHandler = consumableDelegate.errorHandler {
        handleError(it)
    }

    override val consumableUiState: StateFlow<CategoryBuilderConsumables> = consumableDelegate.asStateFlow()
    override val uiStateOutput: StateFlow<CategoryBuilderUiState> = getCategoryBuilder[step]
        .map(::CategoryBuilderUiState)
        .catchIn(errorHandler)
        .flowOn(default)
        .stateInRetained(viewModelScope, CategoryBuilderUiState())

    /**
     * Selects the manually added item at the given [position].
     * @param position The position of the selected item.
     */
    fun selectManuallyAddedItem(position: Int) = errorHandler.catching {
        consumableDelegate.update {
            it.selectManualItem(uiStateOutput.value.manuallyAdded[position])
        }
    }

    /**
     * Selects the suggested item at the given [position].
     * @param position The position of the selected item.
     */
    fun selectSuggestedItem(position: Int) = errorHandler.catching {
        consumableDelegate.update {
            it.selectUserSuggestion(uiStateOutput.value.suggestions[position])
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
        val builder = checkNotNull(uiStateOutput.value.builder)
        try {
            val result = builder.next()
            consumableDelegate.update {
                it.navigateToNext(result)
            }
        } catch (e: NotEnoughInputsException) {
            // avoid logging the exception as an error
            analyticsReporter.log(NotEnoughItemsToCompleteEvent(builder.id, e.remainingInputs))
            consumableDelegate.update {
                it.handleError(e)
            }
        }
    }
}
