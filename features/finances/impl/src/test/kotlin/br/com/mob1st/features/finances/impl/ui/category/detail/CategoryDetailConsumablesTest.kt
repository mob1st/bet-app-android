package br.com.mob1st.features.finances.impl.ui.category.detail

import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CategoryDetailConsumablesTest {
    @Test
    fun `GIVEN a fixed recurrence WHEN show edit recurrences dialog THEN assert it is fixed dialog with the selected day`() {
        val consumables = CategoryDetailConsumables()
        val actual = consumables.showRecurrencesDialog(
            Recurrences.Fixed(DayOfMonth(3)),
        )
        val expected = CategoryDetailConsumables(
            dialog = EditRecurrencesDialog.Fixed(
                selected = 2,
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a seasonal recurrence WHEN show edit recurrences dialog THEN assert it is seasonal dialog`() {
        val consumables = CategoryDetailConsumables()
        val actual = consumables.showRecurrencesDialog(
            Recurrences.Seasonal(
                daysOfYear = listOf(DayOfYear(50)),
            ),
        )
        val expected = CategoryDetailConsumables(
            dialog = EditRecurrencesDialog.Seasonal(
                selected = persistentListOf(1),
            ),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a variable recurrence WHEN show edit recurrences dialog THEN assert it is variable dialog`() {
        val consumables = CategoryDetailConsumables()
        val actual = consumables.showRecurrencesDialog(Recurrences.Variable)
        val expected = CategoryDetailConsumables(dialog = VariableNotAllowEditionDialog)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a selected icon WHEN show icon picker dialog THEN assert it is icon picker dialog`() {
        val consumables = CategoryDetailConsumables()
        val actual = consumables.showIconPickerDialog(Uri("icon"))
        val expected = CategoryDetailConsumables(dialog = IconPickerDialog(Uri("icon")))
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a initial name WHEN show enter name dialog THEN assert show dialog with empty name`() {
        val consumables = CategoryDetailConsumables()
        val actual = consumables.showEnterCategoryNameDialog("name")
        val expected = CategoryDetailConsumables(dialog = EditCategoryNameDialog("name"))
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a initial name WHEN show enter name dialog And set a new name THEN assert dialog has the new name`() {
        val consumables = CategoryDetailConsumables()
        val actual = consumables.showEnterCategoryNameDialog("name").setName("new name")
        val expected = CategoryDetailConsumables(dialog = EditCategoryNameDialog("new name"))
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a initial state WHEN set dialog name THEN assert error is thrown`() {
        val consumables = CategoryDetailConsumables()
        assertThrows<IllegalStateException> { consumables.setName("new name") }
    }
}
