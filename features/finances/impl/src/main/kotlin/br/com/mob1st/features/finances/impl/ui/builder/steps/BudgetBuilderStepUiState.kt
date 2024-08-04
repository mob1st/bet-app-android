package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.utils.components.CategorySectionItemState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

/**
 * The UI state for the category builder screen.
 */
@Immutable
internal sealed interface BudgetBuilderStepUiState {
    /**
     * The initial UI state for the category builder screen.
     */
    @Immutable
    data object Empty : BudgetBuilderStepUiState

    /**
     * The root UI state for the category builder screen.
     * @property builder The category builder. It's loaded from the domain layer and the initial value is null.
     */
    @Immutable
    data class Loaded(
        val builder: BudgetBuilder,
        val isLoadingNext: Boolean = false,
    ) : BudgetBuilderStepUiState {
        constructor(step: BuilderNextAction.Step) : this(
            builder = BudgetBuilder(step, emptyList()),
        )

        /**
         * Indicates if the categories have been loaded.
         */
        val hasLoaded: Boolean = builder.categories.isNotEmpty()

        val header: Header = when (builder.id) {
            FixedExpensesStep -> Header(
                title = R.string.finances_builder_fixed_expenses_header,
                description = R.string.finances_builder_fixed_expenses_subheader,
            )

            FixedIncomesStep -> Header(
                title = R.string.finances_builder_fixed_incomes_header,
                description = R.string.finances_builder_fixed_incomes_subheader,
            )

            VariableExpensesStep -> Header(
                title = R.string.finances_builder_variable_expenses_header,
                description = R.string.finances_builder_variable_expenses_subheader,
            )

            SeasonalExpensesStep -> Header(
                title = R.string.finances_builder_step_seasonal_expenses_title,
                description = R.string.finances_builder_seasonal_expenses_subheader,
            )
        }

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

        @Immutable
        internal data class Header(
            @StringRes val title: Int,
            @StringRes val description: Int,
        )
    }
}
