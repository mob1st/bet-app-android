package br.com.mob1st.features.finances.impl.ui.category.detail

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.CalculatorPreferences
import br.com.mob1st.features.finances.impl.domain.fixtures.categoryDetail
import br.com.mob1st.features.finances.impl.ui.fixtures.categoryEntry
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CategoryDetailUiStateTest {
    @Test
    fun `GIVEN an amount and cents edit not enabled WHEN append THEN assert number is appended as currency`() {
        val detail = Arb.categoryDetail().next().copy(
            preferences = Arb.bind<CalculatorPreferences>().next().copy(
                isCentsEnabled = false,
            ),
        )
        val entry = Arb.categoryEntry().next().copy(
            amount = Money(100),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = detail,
            entry = entry,
        )
        val actual = uiState.appendNumber(2)
        assertEquals(
            entry.copy(
                amount = Money(1000 + 200),
            ),
            actual,
        )
    }

    @Test
    fun `GIVEN an amount and cents edit enabled WHEN append number THEN assert number is appended as cents`() {
        val detail = Arb.categoryDetail().next().copy(
            preferences = Arb.bind<CalculatorPreferences>().next().copy(
                isCentsEnabled = true,
            ),
        )
        val entry = Arb.categoryEntry().next().copy(
            amount = Money(1),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = detail,
            entry = entry,
        )
        val actual = uiState.appendNumber(2)
        assertEquals(
            entry.copy(
                amount = Money(12),
            ),
            actual,
        )
    }

    @Test
    fun `GIVEN an amount and cents enabled WHEN erase a number THEN assert new amount is divided by 10`() {
        val entry = Arb.categoryEntry().next().copy(
            amount = Money(1100),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next(),
            entry = entry,
        )
        val actual = uiState.erase()
        assertEquals(
            entry.copy(
                amount = Money(110),
            ),
            actual,
        )
    }

    @Test
    fun `GIVEN an amount and edit cents disabled WHEN erase a number THEN assert new amount is divided by 10 And rounded`() {
        val entry = Arb.categoryEntry().next().copy(
            amount = Money(1100),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next().copy(
                preferences = Arb.bind<CalculatorPreferences>().next().copy(
                    isCentsEnabled = false,
                ),
            ),
            entry = entry,
        )
        val actual = uiState.erase()
        assertEquals(
            entry.copy(
                amount = Money(100),
            ),
            actual,
        )
    }

    @Test
    fun `GIVEN a edit cents enabled WHEN toggle decimal mode THEN assert amount is multiplied by 100 And disable cents edit`() {
        val entry = Arb.categoryEntry().next().copy(
            amount = Money(1),
        )
        val detail = Arb.categoryDetail().next().copy(
            preferences = Arb.bind<CalculatorPreferences>().next().copy(
                isCentsEnabled = true,
            ),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = detail,
            entry = entry,
        )
        val actual = uiState.toggleDecimalMode()
        val expected = actual.copy(
            entry = entry.copy(
                amount = Money(100),
            ),
            detail = detail.copy(
                preferences = detail.preferences.copy(
                    isCentsEnabled = false,
                ),
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a edit cents disabled WHEN toggle decimal mode THEN assert amount is divided by 100 And enable cents edit`() {
        val entry = Arb.categoryEntry().next().copy(
            amount = Money(100),
        )
        val detail = Arb.categoryDetail().next().copy(
            preferences = Arb.bind<CalculatorPreferences>().next().copy(
                isCentsEnabled = false,
            ),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = detail,
            entry = entry,
        )
        val actual = uiState.toggleDecimalMode()
        val expected = actual.copy(
            entry = entry.copy(
                amount = Money(1),
            ),
            detail = detail.copy(
                preferences = detail.preferences.copy(
                    isCentsEnabled = true,
                ),
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN an entry different than category WHEN merge THEN assert new category has entry changes`() {
        val entry = Arb.categoryEntry().next()
        val categoryDetail = Arb.categoryDetail().next()
        val uiState = CategoryDetailUiState.Loaded(
            detail = categoryDetail,
            entry = entry,
        )
        val actual = uiState.merge()
        val expected = categoryDetail.category.copy(
            name = entry.name,
            amount = entry.amount,
            image = entry.image,
            recurrences = entry.recurrences,
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a enter name dialog WHEN submit THEN assert name is updated`() {
        val entry = Arb.categoryEntry().next().copy(
            name = "old name",
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next(),
            entry = entry,
        )
        val dialog = EditCategoryNameDialog("new name")
        val actual = uiState.submitDialog(dialog)
        val expected = entry.copy(
            name = dialog.name,
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a icon picker dialog WHEN submit THEN assert image is updated`() {
        val entry = Arb.categoryEntry().next().copy(
            image = Uri("old_image"),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next(),
            entry = entry,
        )
        val dialog = IconPickerDialog(Uri("new_image"))
        val actual = uiState.submitDialog(dialog)
        val expected = entry.copy(
            image = dialog.selected,
        )
        assertEquals(expected, actual)
    }
}
