package br.com.mob1st.features.finances.impl.ui.builder.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInWhileSubscribed
import br.com.mob1st.core.state.contracts.UiStateOutputManager
import br.com.mob1st.core.state.managers.ConsumableDelegate
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.launchIn
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.usecases.StartBuilderStepUseCase
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.utils.states.errorHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class BuilderIntroViewModel private constructor(
    private val startBuilderStep: StartBuilderStepUseCase,
    private val router: BuilderRouter,
    private val consumableDelegate: ConsumableDelegate<BuilderIntroConsumables>,
) : ViewModel(),
    UiStateOutputManager<BuilderIntroUiState>,
    ConsumableManager<BuilderIntroConsumables> by consumableDelegate {
    constructor(
        startBuilderStep: StartBuilderStepUseCase,
        router: BuilderRouter,
    ) : this(
        startBuilderStep = startBuilderStep,
        router = router,
        consumableDelegate = ConsumableDelegate(BuilderIntroConsumables()),
    )

    private val isLoadingState = MutableStateFlow(false)
    private val errorHandler = consumableDelegate.errorHandler {
        handleError(it)
    }

    override val uiStateOutput: StateFlow<BuilderIntroUiState> = isLoadingState.map {
        BuilderIntroUiState(it)
    }.stateInWhileSubscribed(viewModelScope, BuilderIntroUiState(false))

    fun start() = launchIn(errorHandler) {
        isLoadingState.value = true
        val step = BudgetBuilder.firstStep()
        try {
            startBuilderStep(step)
        } finally {
            isLoadingState.value = false
        }
        consumableDelegate.update {
            it.copy(route = router.send(step))
        }
    }
}
