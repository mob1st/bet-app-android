package br.com.mob1st.features.finances.impl.ui.builder.steps

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth

internal object BudgetBuilderStepPreviewFixture {
    val uiState = BudgetBuilderStepUiState.Packed(
        builder = BudgetBuilder(
            id = FixedIncomesStep,
            manuallyAdded = listOf(
                Category(
                    id = Category.Id(43),
                    name = "Category 1",
                    amount = Money(48558),
                    isExpense = true,
                    recurrences = Recurrences.Fixed(DayOfMonth(1)),
                ),
            ),
            suggestions = listOf(
                CategorySuggestion(
                    id = CategorySuggestion.Id(9),
                    nameResId = R.string.finances_builder_suggestions_item_gym,
                    linkedCategory = null,
                ),
                CategorySuggestion(
                    id = CategorySuggestion.Id(6),
                    nameResId = R.string.finances_builder_suggestions_item_night_clubs,
                    linkedCategory = Category(
                        id = Category.Id(1),
                        name = "Night Clubs",
                        amount = Money(50000),
                        isExpense = true,
                        recurrences = Recurrences.Fixed(DayOfMonth(1)),
                    ),
                ),
                CategorySuggestion(
                    id = CategorySuggestion.Id(2),
                    nameResId = R.string.finances_builder_suggestions_item_rent_or_mortgage,
                    linkedCategory = null,
                ),
                CategorySuggestion(
                    id = CategorySuggestion.Id(),
                    nameResId = R.string.finances_builder_suggestions_item_health_insurance,
                    linkedCategory = Category(
                        id = Category.Id(3),
                        name = "Health Insurance",
                        amount = Money(124056),
                        isExpense = true,
                        recurrences = Recurrences.Fixed(DayOfMonth(1)),
                    ),
                ),
            ),
        ),
    )
    val consumables = BudgetBuilderStepConsumables()
}
