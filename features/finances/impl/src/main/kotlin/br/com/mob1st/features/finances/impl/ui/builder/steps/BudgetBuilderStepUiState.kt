package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyTextState
import br.com.mob1st.features.finances.impl.ui.utils.texts.RecurrencesTextStateFactory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

/**
 * The UI state for the category builder screen.
 */
@Immutable
internal sealed interface BudgetBuilderStepUiState

/**
 * The initial UI state for the category builder screen.
 */
@Immutable
internal data object EmptyBudgetBuilderStepUiState : BudgetBuilderStepUiState

/**
 * The root UI state for the category builder screen.
 * @property builder The category builder. It's loaded from the domain layer and the initial value is null.
 */
@Immutable
internal data class FilledBudgetBuilderStepUiState(
    val builder: BudgetBuilder,
) : BudgetBuilderStepUiState {

    val header: Header = when (builder.id) {
        FixedExpensesStep -> Header(
            title = 0,
            description = 0
        )

        FixedIncomesStep -> Header(
            title = 0,
            description = 0
        )

        VariableExpensesStep -> Header(
            title = 0,
            description = 0
        )
    }

    /**
     * The manually added categories.
     * The list is composed of the manually added categories and the "Add category" item.
     */
    val manuallyAdded: ImmutableList<CategoryListItem> = builder.manuallyAdded.map {
        ManualCategoryListItem(it)
    }.toPersistentList() + AddCategoryListItem

    /**
     * The suggestions presented to the user.
     */
    val suggestions: ImmutableList<SuggestionListItem> = builder.suggestions.map {
        SuggestionListItem(it)
    }.toImmutableList()

    @Immutable
    internal data class Header(
        @StringRes val title: Int,
        @StringRes val description: Int,
    )
}

/**
 * Abstraction of a list item to present in the category builder screen.
 * It can be a suggestion, a manually added category, or the "Add category" item.
 */
@Immutable
sealed interface CategoryListItem {
    /**
     * The leading text of the item. Usually it's the category name or the main instruction
     */
    val leading: TextState

    /**
     * The value text of the item. It's usually the category amount, if any.
     */
    val value: TextState?

    /**
     * The supporting text of the item. Some category types can use it to describe the total amount per month.
     */
    val supporting: TextState?
}

/**
 * Used by the [BudgetBuilderStepUiState.suggestions] to show the automatic suggestions provided by the app.
 * It's a handy way to facilitate the user's choice.
 * @property suggestion The suggestion.
 */
@Immutable
data class SuggestionListItem(
    val suggestion: CategorySuggestion,
) : CategoryListItem {
    override val leading: TextState = if (suggestion.linkedCategory != null) {
        TextState(suggestion.linkedCategory.name)
    } else {
        TextState(suggestion.nameResId)
    }
    override val value: TextState? = suggestion.linkedCategory?.amount?.let {
        MoneyTextState(it)
    }

    override val supporting: TextState? = suggestion.linkedCategory?.recurrences?.let {
        RecurrencesTextStateFactory.create(it)
    }
}

/**
 * Used by the [BudgetBuilderStepUiState.manuallyAdded] to show the manually added categories.
 * @property category The manually added category.
 */
@Immutable
data class ManualCategoryListItem(val category: Category) : CategoryListItem {
    override val leading: TextState = TextState(category.name)
    override val value: TextState = MoneyTextState(category.amount)
    override val supporting: TextState? = RecurrencesTextStateFactory.create(category.recurrences)
}

/**
 * Used by the [BudgetBuilderStepUiState.manuallyAdded] to show the "Add category" item.
 */
@Immutable
internal data object AddCategoryListItem : CategoryListItem {
    override val leading: TextState = TextState(0)
    override val value: TextState? = null
    override val supporting: TextState? = null
}
