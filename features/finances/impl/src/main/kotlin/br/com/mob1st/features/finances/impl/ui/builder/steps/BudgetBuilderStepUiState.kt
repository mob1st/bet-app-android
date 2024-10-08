package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.category.components.item.CategorySectionItemState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

/**
 * The UI state for the category builder screen.
 */
@Immutable
internal data class BudgetBuilderStepUiState(
    val step: BudgetBuilderAction.Step,
    val body: Body = BuilderStepLoadingBody,
    val isLoadingNext: Boolean = false,
) {
    val header = BuilderStepHeader.create(step)

    constructor(builder: BudgetBuilder, isLoadingNext: Boolean) : this(
        step = builder.id,
        body = BuilderStepLoadedBody(builder),
        isLoadingNext = isLoadingNext,
    )

    sealed interface Body
}

data object BuilderStepLoadingBody : BudgetBuilderStepUiState.Body

/**
 * The root UI state for the category builder screen.
 * @property builder The category builder. It's loaded from the domain layer and the initial value is null.
 */
@Immutable
data class BuilderStepLoadedBody(
    val builder: BudgetBuilder,
) : BudgetBuilderStepUiState.Body {
    /**
     * The manually added categories.
     * The list is composed of the manually added categories and the "Add category" item.
     */
    val manuallyAdded: ImmutableList<CategorySectionItemState> = builder.manuallyAdded.map {
        CategorySectionItemState(it)
    }.toPersistentList()

    /**
     * The suggestions presented to the user.
     */
    val suggestions: ImmutableList<CategorySectionItemState> = builder.suggestions.map {
        CategorySectionItemState(it)
    }.toImmutableList()
}

@Immutable
internal data class BuilderStepHeader(
    @StringRes val title: Int,
    @StringRes val description: Int,
) {
    companion object {
        fun create(step: BudgetBuilderAction.Step) = when (step) {
            FixedExpensesStep -> BuilderStepHeader(
                title = R.string.finances_builder_fixed_expenses_header,
                description = R.string.finances_builder_fixed_expenses_subheader,
            )

            FixedIncomesStep -> BuilderStepHeader(
                title = R.string.finances_builder_fixed_incomes_header,
                description = R.string.finances_builder_fixed_incomes_subheader,
            )

            VariableExpensesStep -> BuilderStepHeader(
                title = R.string.finances_builder_variable_expenses_header,
                description = R.string.finances_builder_variable_expenses_subheader,
            )

            SeasonalExpensesStep -> BuilderStepHeader(
                title = R.string.finances_builder_step_seasonal_expenses_title,
                description = R.string.finances_builder_seasonal_expenses_subheader,
            )
        }
    }
}
