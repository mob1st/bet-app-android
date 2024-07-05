package br.com.mob1st.features.finances.impl.ui.utils.texts

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class FixedRecurrencesTextStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN a recurrence WHEN resolve THEN assert date is displayed`() {
        val recurrence = Recurrences.Fixed(DayOfMonth(1))
        val textState = FixedRecurrencesTextState(recurrence)
        composeTestRule.setContent {
            val text = textState.resolve()
            assertEquals(
                "Every 1st day",
                text,
            )
        }
    }
}
