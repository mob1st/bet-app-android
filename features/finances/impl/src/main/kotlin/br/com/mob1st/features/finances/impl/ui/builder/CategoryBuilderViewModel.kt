package br.com.mob1st.features.finances.impl.ui.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.Lens
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.state.contracts.UiStateOutputManager
import br.com.mob1st.core.state.managers.ConsumableManager
import br.com.mob1st.core.state.managers.ErrorHandler
import br.com.mob1st.core.state.managers.catchIn
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryBuilderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update

internal class CategoryBuilderViewModel(
    step: BuilderNextAction.Step,
    getCategoryBuilder: GetCategoryBuilderUseCase,
    default: DefaultCoroutineDispatcher,
) : ViewModel(),
    UiStateOutputManager<CategoryBuilderUiState>,
    ConsumableManager<CategoryBuilderConsumables> {
    private val consumables = MutableStateFlow(CategoryBuilderConsumables())
    override val uiStateOutput: StateFlow<CategoryBuilderUiState> = combine(
        getCategoryBuilder[step],
        consumables,
        ::CategoryBuilderUiState,
    ).catchIn(ErrorHandler())
        .flowOn(default)
        .stateInRetained(viewModelScope, CategoryBuilderUiState())

    override fun <Property> consume(lens: Lens<CategoryBuilderConsumables, Property?>) {
        CategoryBuilderConsumables
        consumables.update {
            it
        }
    }
}
