package br.com.mob1st.features.finances.impl.ui.builder.steps

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.Category
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
        isSuggested = false,
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
    )

    private val category2 = Category(
        id = Category.Id(1),
        name = "Night Clubs",
        amount = Money(50000),
        isExpense = true,
        recurrences = Recurrences.Fixed(DayOfMonth(1)),
        isSuggested = false,
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
    )

    private val category3 = Category(
        id = Category.Id(3),
        name = "Health Insurance",
        amount = Money(124056),
        isExpense = true,
        recurrences = Recurrences.Fixed(DayOfMonth(1)),
        isSuggested = true,
        image = Uri("file:///android_asset/icons/finances_builder_suggestions_item_back_to_school_supplies.svg"),
    )

    val uiState = BudgetBuilderStepUiState.Loaded(
        builder = BudgetBuilder(
            id = FixedIncomesStep,
            categories = listOf(
                category1,
                category2,
                category3,
            ),
        ),
    )
    val consumables = BuilderStepConsumables()
}
