package br.com.mob1st.features.finances.impl.ui.category.detail

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.CalculatorPreferences
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.categoryDetail
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import br.com.mob1st.features.finances.impl.ui.fixtures.categoryEntry
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyLocalizedText
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CategoryDetailUiStateTest {
    @Test
    fun `GIVEN an amount and cents edit not enabled WHEN append THEN assert number is appended as currency`() {
        val detail = Arb.categoryDetail().next().copy(
            preferences = Arb.bind<CalculatorPreferences>().next().copy(
                isEditCentsEnabled = false,
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
                isEditCentsEnabled = true,
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
            detail = Arb.categoryDetail().next().copy(
                preferences = Arb.bind<CalculatorPreferences>().next().copy(
                    isEditCentsEnabled = true,
                ),
            ),
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
                    isEditCentsEnabled = false,
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
                isEditCentsEnabled = true,
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
                    isEditCentsEnabled = false,
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
                isEditCentsEnabled = false,
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
                    isEditCentsEnabled = true,
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

    @Test
    fun `GIVEN a fixed recurrences dialog WHEN submit THEN assert recurrences is updated`() {
        val initialDay = DayOfMonth(1)
        val entry = Arb.categoryEntry().next().copy(
            recurrences = Recurrences.Fixed(initialDay),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next(),
            entry = entry,
        )
        // index 1 is the second day of the month
        val dialog = EditRecurrencesDialog.Fixed(selected = 1)
        val actual = uiState.submitDialog(dialog)
        val expected = entry.copy(
            recurrences = Recurrences.Fixed(
                DayOfMonth(2),
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a seasonal recurrences dialog WHEN submit THEN assert recurrences is updated`() {
        val initialDays = listOf(
            DayOfYear(1),
            DayOfYear(32),
        )
        val entry = Arb.categoryEntry().next().copy(
            recurrences = Recurrences.Seasonal(
                daysOfYear = initialDays,
            ),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next(),
            entry = entry,
        )
        // index 1 is the second month of the year
        val dialog = EditRecurrencesDialog.Seasonal(
            selected = persistentListOf(2, 3),
        )
        val actual = uiState.submitDialog(dialog)
        val expected = entry.copy(
            recurrences = Recurrences.Seasonal(
                daysOfYear = listOf(
                    DayOfYear(60),
                    DayOfYear(91),
                ),
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a variable not allow edition dialog WHEN submit THEN assert entry is not updated`() {
        val entry = Arb.categoryEntry().next()
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next(),
            entry = entry,
        )
        val dialog = VariableNotAllowEditionDialog
        val actual = uiState.submitDialog(dialog)
        assertEquals(entry, actual)
    }

    @Test
    fun `GIVEN an entry WHEN get the name THEN assert name is returned`() {
        val entry = Arb.categoryEntry().next().copy(
            name = "name",
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next(),
            entry = entry,
        )
        val actual = uiState.name
        assertEquals(entry.name, actual)
    }

    @Test
    fun `GIVEN a entry WHEN get the amount THEN assert amount is returned`() {
        val entry = Arb.categoryEntry().next().copy(
            amount = Money(100),
        )
        val uiState = CategoryDetailUiState.Loaded(
            detail = Arb.categoryDetail().next(),
            entry = entry,
        )
        val actual = uiState.amount
        val expected = MoneyLocalizedText(entry.amount)
        assertEquals(expected, actual)
    }
}
