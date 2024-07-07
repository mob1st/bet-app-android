package br.com.mob1st.features.finances.impl.ui.builder.steps

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

class BudgetBuilderStepDialogTest {
    @Test
    fun `GIVEN only 2 characters WHEN check if button is enabled THEN returns false`() {
        val dialog = BudgetBuilderStepDialog.EnterName("ab")
        assertFalse(dialog.isSubmitEnabled)
    }

    @Test
    fun `GIVEN 3 characters WHEN check if button is enabled THEN returns true`() {
        val dialog = BudgetBuilderStepDialog.EnterName("abc")
        assertTrue(dialog.isSubmitEnabled)
    }

    @Test
    fun `GIVEN blank characters WHEN check if button is enabled THEN returns true`() {
        val dialog = BudgetBuilderStepDialog.EnterName("    ")
        assertFalse(dialog.isSubmitEnabled)
    }
}
