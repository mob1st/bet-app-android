package br.com.mob1st.features.finances.impl.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mob1st.core.androidx.flows.stateInRetained
import br.com.mob1st.core.kotlinx.coroutines.asResultFlow
import br.com.mob1st.core.kotlinx.structures.Either
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategoryGroup
import br.com.mob1st.features.finances.publicapi.domain.usecases.GetFixedExpensesUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

internal class AddFixedRecurrenceViewModel(
    getFixedExpensesUseCase: GetFixedExpensesUseCase,
) : ViewModel() {

    val uiState: StateFlow<AddFixedRecurrenceUiState> = getFixedExpensesUseCase()
        .asResultFlow()
        .map(AddFixedRecurrenceUiState.Companion::transform)
        .stateInRetained(viewModelScope, AddFixedRecurrenceUiState.Empty())
}

data class FixedRecurrenceManualInput(
    val description: String,
    val amount: String,
    val dayOfMonth: Int,
)

@Immutable
sealed interface AddFixedRecurrenceUiState :
    Either<AddFixedRecurrenceUiState.Empty, AddFixedRecurrenceUiState.Filled> {

    data class Empty(val failed: Boolean = false) : AddFixedRecurrenceUiState
    data class Filled(
        val recurrenceSuggestion: ImmutableList<RecurrentCategory.Fixed>,
        val selectedManualInput: FixedRecurrenceManualInput? = null,
    ) : AddFixedRecurrenceUiState

    companion object {
        fun transform(suggestionsResult: Result<RecurrentCategoryGroup<RecurrentCategory.Fixed>>) = suggestionsResult
            .fold(
                onSuccess = {
                    Filled(it.items.toImmutableList())
                },
                onFailure = {
                    Empty(failed = true)
                }
            )
    }
}
