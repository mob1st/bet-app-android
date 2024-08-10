package br.com.mob1st.features.finances.impl.ui.builder.steps

import br.com.mob1st.features.finances.impl.ui.category.components.dialog.CategoryNameDialogState
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

class CategoryNameDialogStateTest {
    @Test
    fun `GIVEN only 2 characters WHEN check if button is enabled THEN returns false`() {
        val dialog = CategoryNameDialogState("ab")
        assertFalse(dialog.isSubmitEnabled)
    }

    @Test
    fun `GIVEN 3 characters WHEN check if button is enabled THEN returns true`() {
        val dialog = CategoryNameDialogState("abc")
        assertTrue(dialog.isSubmitEnabled)
    }

    @Test
    fun `GIVEN blank characters WHEN check if button is enabled THEN returns true`() {
        val dialog = CategoryNameDialogState("    ")
        assertFalse(dialog.isSubmitEnabled)
    }
}
