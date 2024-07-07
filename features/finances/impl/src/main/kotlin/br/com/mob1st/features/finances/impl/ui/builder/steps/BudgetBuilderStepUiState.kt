package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.features.finances.impl.R
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
import org.koin.ext.getFullName

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
    data class Packed(
        val builder: BudgetBuilder,
    ) : BudgetBuilderStepUiState {
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
}

/**
 * Abstraction of a list item to present in the category builder screen.
 * It can be a suggestion, a manually added category, or the "Add category" item.
 */
@Immutable
sealed interface CategoryListItem {
    /**
     * The key of the item. It's used to identify the item in the list and optimize the rendering.
     */
    val key: Any

    /**
     * The leading text of the item. Usually it's the category name or the main instruction
     */
    val headline: TextState

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
 * Used by the [BudgetBuilderStepUiState.Packed.suggestions] to show the automatic suggestions provided by the app.
 * It's a handy way to facilitate the user's choice.
 * @property suggestion The suggestion.
 */
@Immutable
data class SuggestionListItem(
    val suggestion: CategorySuggestion,
) : CategoryListItem {
    override val key: Any = suggestion.id
    override val headline: TextState = if (suggestion.linkedCategory != null) {
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
 * Used by the [BudgetBuilderStepUiState.Packed.manuallyAdded] to show the manually added categories.
 * @property category The manually added category.
 */
@Immutable
data class ManualCategoryListItem(val category: Category) : CategoryListItem {
    override val key: Any = category.id
    override val headline: TextState = TextState(category.name)
    override val value: TextState = MoneyTextState(category.amount)
    override val supporting: TextState? = RecurrencesTextStateFactory.create(category.recurrences)
}

/**
 * Used by the [BudgetBuilderStepUiState.Packed.manuallyAdded] to show the "Add category" item.
 */
@Immutable
internal data object AddCategoryListItem : CategoryListItem {
    override val key: Any = AddCategoryListItem::class.getFullName()
    override val headline: TextState = TextState(R.string.finances_builder_commons_custom_section_add_item)
    override val value: TextState? = null
    override val supporting: TextState? = null
}
