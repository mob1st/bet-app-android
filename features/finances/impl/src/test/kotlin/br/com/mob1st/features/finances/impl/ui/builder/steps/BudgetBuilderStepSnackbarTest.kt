package br.com.mob1st.features.finances.impl.ui.builder.steps

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class BudgetBuilderStepSnackbarTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `GIVEN a single remaining item WHEN get snackbar message THEN assert it use singular`() {
        val snackbarState = BudgetBuilderStepSnackbar.NotAllowedToProceed(1)
        composeRule.setContent {
            val visuals = snackbarState.resolve()
            assertEquals(
                "You need to add more 1 category to proceed",
                visuals.message,
            )
        }
    }

    @Test
    fun `GIVEN multiple remaining items WHEN get snackbar message THEN assert it use plural`() {
        val snackbarState = BudgetBuilderStepSnackbar.NotAllowedToProceed(2)
        composeRule.setContent {
            val visuals = snackbarState.resolve()
            assertEquals(
                "You need to add more 2 categories to proceed",
                visuals.message,
            )
        }
    }
}
