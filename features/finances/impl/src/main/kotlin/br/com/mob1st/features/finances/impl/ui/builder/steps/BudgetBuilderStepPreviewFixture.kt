package br.com.mob1st.features.finances.impl.ui.builder.steps

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth

@Suppress("MagicNumber")
internal object BudgetBuilderStepPreviewFixture {
    private val category1 = Category(
        id = Category.Id(43),
        name = "Category 1",
        amount = Money(48558),
        isExpense = true,
        recurrences = Recurrences.Fixed(DayOfMonth(1)),
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
    )

    private val category2 = Category(
        id = Category.Id(1),
        name = "Night Clubs",
        amount = Money(50000),
        isExpense = true,
        recurrences = Recurrences.Fixed(DayOfMonth(1)),
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
    )

    private val category3 = Category(
        id = Category.Id(3),
        name = "Health Insurance",
        amount = Money(124056),
        isExpense = true,
        recurrences = Recurrences.Fixed(DayOfMonth(1)),
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
    )

    private val suggestion1 = CategorySuggestion(
        id = CategorySuggestion.Id(9),
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
        nameResId = R.string.finances_builder_suggestions_item_gym,
        linkedCategory = null,
    )

    private val suggestion2 = CategorySuggestion(
        id = CategorySuggestion.Id(6),
        nameResId = R.string.finances_builder_suggestions_item_night_clubs,
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
        linkedCategory = category2,
    )

    private val suggestion3 = CategorySuggestion(
        id = CategorySuggestion.Id(2),
        nameResId = R.string.finances_builder_suggestions_item_rent_or_mortgage,
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
        linkedCategory = null,
    )

    private val suggestion4 = CategorySuggestion(
        id = CategorySuggestion.Id(),
        nameResId = R.string.finances_builder_suggestions_item_health_insurance,
        image = Uri("test://image4"),
        linkedCategory = category3,
    )

    val uiState = BudgetBuilderStepUiState.Loaded(
        builder = BudgetBuilder(
            id = FixedIncomesStep,
            manuallyAdded = listOf(category1),
            suggestions = listOf(
                suggestion1,
                suggestion2,
                suggestion3,
                suggestion4,
            ),
        ),
    )
    val consumables = BudgetBuilderStepConsumables()
}
