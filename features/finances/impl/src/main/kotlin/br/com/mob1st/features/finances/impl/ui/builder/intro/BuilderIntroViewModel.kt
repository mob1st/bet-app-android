package br.com.mob1st.features.finances.impl.ui.builder.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.viewmodels.launchIn
import br.com.mob1st.core.kotlinx.coroutines.stateInWhileSubscribed
import br.com.mob1st.core.state.managers.AsyncLoadingState
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.UiStateManager
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.usecases.StartBuilderStepUseCase
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.utils.errors.commonErrorHandler
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class BuilderIntroViewModel(
    private val consumableDelegate: ConsumableDelegate<BuilderIntroConsumables>,
    private val router: BuilderRouter,
    private val startBuilderStep: StartBuilderStepUseCase,
) : ViewModel(),
    UiStateManager<BuilderIntroUiState>,
    ConsumableManager<BuilderIntroConsumables> by consumableDelegate {
    private val isLoadingState = AsyncLoadingState()
    private val errorHandler = consumableDelegate.commonErrorHandler {
        copy(snackbar = it)
    }

    override val uiState: StateFlow<BuilderIntroUiState> = isLoadingState.map {
        BuilderIntroUiState(it)
    }.stateInWhileSubscribed(
        viewModelScope,
        BuilderIntroUiState(false),
    )

    /**
     * Starts the builder flow triggering the navigation to the first step.
     */
    fun start() = launchIn(errorHandler) {
        val step = BudgetBuilder.firstStep()
        isLoadingState.trigger {
            startBuilderStep(step)
        }
        consumableDelegate.update {
            it.copy(route = router.to(step))
        }
    }

    companion object {
        /**
         * Creates a new instance of [BuilderIntroViewModel].
         */
        fun consumableDelegate() = ConsumableDelegate(BuilderIntroConsumables())
    }
}
