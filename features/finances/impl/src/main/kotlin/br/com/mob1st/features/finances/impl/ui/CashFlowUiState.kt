package br.com.mob1st.features.finances.impl.ui

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface CashFlowUiState {
    @Immutable
    data object Empty : CashFlowUiState

    @Immutable
    data class Loaded(
        val slider: TransactionsSliderState,
        val summarySection: SummarySectionState,
    ) : CashFlowUiState
}

@Immutable
data class SummarySectionState(
    val currentValue: String,
    val endOfTheWeek: String,
    val endOfTheMonth: String,
)

@Immutable
data class TransactionsSliderState(
    val currentPage: Int,
    val pages: ImmutableList<Page>,
) {
    @Immutable
    data class NotPlannedItem(
        val description: String,
        val date: String,
        val value: String,
    )

    @Immutable
    data class Page(
        val date: String,
        val planned: ImmutableList<PlannedItem>,
        val notPlanned: ImmutableList<NotPlannedItem>,
    )

    @Immutable
    data class PlannedItem(
        val description: String,
        val plannedValue: String,
        val actualValue: String?,
        val isFollowingThePlan: Boolean,
        val category: String,
        val isPlanned: Boolean,
        val isExpense: Boolean,
    ) {
        val displayedValue: String = actualValue ?: plannedValue
    }
}

@Immutable
sealed interface ProfileState {
    @Immutable
    @JvmInline
    value class User(val imageUrl: String) : ProfileState

    @Immutable
    data object Guest : ProfileState
}
