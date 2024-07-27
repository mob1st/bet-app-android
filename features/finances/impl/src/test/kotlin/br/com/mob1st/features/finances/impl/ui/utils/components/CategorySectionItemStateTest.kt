package br.com.mob1st.features.finances.impl.ui.utils.components

import br.com.mob1st.core.design.atoms.properties.colors.BackgroundContainerCombination
import br.com.mob1st.core.design.atoms.properties.colors.FixedContainerCombination
import br.com.mob1st.core.design.atoms.properties.colors.IncomesContainerCombination
import br.com.mob1st.core.design.atoms.properties.colors.SeasonalContainerCombination
import br.com.mob1st.core.design.atoms.properties.colors.VariableContainerCombination
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.fixtures.fixedRecurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.seasonalRecurrences
import br.com.mob1st.features.finances.impl.ui.utils.texts.FixedRecurrencesLocalizedText
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyLocalizedText
import br.com.mob1st.features.finances.impl.ui.utils.texts.SeasonalRecurrencesLocalizedText
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CategorySectionItemStateTest {
    @Test
    fun `GIVEN a category with zeroed amount WHEN create state THEN assert icon background is not colored`() {
        val category = givenCategory {
            it.copy(
                amount = Money.Zero,
                isExpense = true,
            )
        }
        val actual = CategorySectionItemState(category)
        assertEquals(
            BackgroundContainerCombination,
            actual.icon.combination,
        )
    }

    @Test
    fun `GIVEN a recurrence with fixed recurrence WHEN create state THEN assert properties`() {
        val recurrence = Arb.fixedRecurrences().next()
        val category = givenCategory {
            it.copy(
                recurrences = recurrence,
                isExpense = true,
            )
        }
        val actual = CategorySectionItemState(category)
        assertProperties(
            expectedIcon = CategorySectionItemState.Icon(
                combination = FixedContainerCombination,
                image = category.image,
            ),
            expectedHeadline = category.name,
            expectedSupporting = FixedRecurrencesLocalizedText(recurrence),
            expectedAmount = CategorySectionItemState.Amount(
                amount = MoneyLocalizedText(category.amount),
                supporting = LocalizedText(R.string.finances_commons__category_item__trailing_support_fixed),
            ),
            actual = actual,
        )
    }

    @Test
    fun `GIVEN a category with variable recurrence WHEN create state THEN assert properties`() {
        val category = givenCategory {
            it.copy(
                recurrences = Recurrences.Variable,
                isExpense = true,
            )
        }
        val actual = CategorySectionItemState(category)
        assertProperties(
            expectedIcon = CategorySectionItemState.Icon(
                combination = VariableContainerCombination,
                image = category.image,
            ),
            expectedHeadline = category.name,
            expectedSupporting = null,
            expectedAmount = CategorySectionItemState.Amount(
                amount = MoneyLocalizedText(category.amount),
                supporting = LocalizedText(
                    R.string.finances_commons__category_item__trailing_support_variable,
                ),
            ),
            actual = actual,
        )
    }

    @Test
    fun `GIVEN a category with seasonal recurrence And no days WHEN create state THEN assert properties`() {
        val category = givenCategory {
            it.copy(
                recurrences = Recurrences.Seasonal(emptyList()),
                isExpense = true,
            )
        }
        val actual = CategorySectionItemState(category)
        assertProperties(
            expectedIcon = CategorySectionItemState.Icon(
                combination = SeasonalContainerCombination,
                image = category.image,
            ),
            expectedHeadline = category.name,
            expectedSupporting = null,
            expectedAmount = CategorySectionItemState.Amount(
                amount = MoneyLocalizedText(category.amount),
                supporting = LocalizedText(
                    R.string.finances_commons__category_item__trailing_support_seasonal,
                ),
            ),
            actual = actual,
        )
    }

    @Test
    fun `GIVEN a category with seasonal recurrence And days WHEN create state THEN assert properties`() {
        val recurrence = Arb.seasonalRecurrences().filter { it.daysOfYear.isNotEmpty() }.next()
        val category = givenCategory {
            it.copy(
                recurrences = recurrence,
                isExpense = true,
            )
        }
        val actual = CategorySectionItemState(category)
        assertProperties(
            expectedIcon = CategorySectionItemState.Icon(
                combination = SeasonalContainerCombination,
                image = category.image,
            ),
            expectedHeadline = category.name,
            expectedSupporting = SeasonalRecurrencesLocalizedText(recurrence),
            expectedAmount = CategorySectionItemState.Amount(
                amount = MoneyLocalizedText(category.amount),
                supporting = LocalizedText(
                    R.string.finances_commons__category_item__trailing_support_seasonal,
                ),
            ),
            actual = actual,
        )
    }

    @Test
    fun `GIVEN a category with fixed income WHEN create state THEN assert properties`() {
        val recurrence = Arb.fixedRecurrences().next()
        val category = givenCategory {
            it.copy(
                recurrences = recurrence,
                isExpense = false,
            )
        }
        val actual = CategorySectionItemState(category)
        assertProperties(
            expectedIcon = CategorySectionItemState.Icon(
                combination = IncomesContainerCombination,
                image = category.image,
            ),
            expectedHeadline = category.name,
            expectedSupporting = FixedRecurrencesLocalizedText(recurrence),
            expectedAmount = CategorySectionItemState.Amount(
                amount = MoneyLocalizedText(category.amount),
                supporting = LocalizedText(
                    R.string.finances_commons__category_item__trailing_support_fixed,
                ),
            ),
            actual = actual,
        )
    }

    private fun givenCategory(block: (category: Category) -> Category): Category {
        return Arb.category().map(block).next()
    }

    private fun assertProperties(
        expectedIcon: CategorySectionItemState.Icon,
        expectedHeadline: String,
        expectedSupporting: LocalizedText?,
        expectedAmount: CategorySectionItemState.Amount,
        actual: CategorySectionItemState,
    ) {
        assertEquals(
            expectedHeadline,
            actual.name,
        )
        assertEquals(
            expectedSupporting,
            actual.recurrences,
        )
        assertEquals(
            expectedAmount,
            actual.amount,
        )
        assertEquals(
            expectedIcon,
            actual.icon,
        )
    }
}
