package br.com.mob1st.features.finances.impl.ui.utils.texts

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import br.com.mob1st.features.finances.impl.domain.values.Month
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class SeasonalRecurrencesTextStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN a recurrence with one date WHEN resolve THEN assert date is displayed`() {
        val recurrence = Recurrences.Seasonal(
            listOf(
                DayOfYear(DayOfMonth(1), Month.June),
            ),
        )
        val textState = SeasonalRecurrencesTextState(recurrence)
        composeTestRule.setContent {
            val text = textState.resolve()
            assertEquals(
                "JUN",
                text,
            )
        }
    }

    @Test
    fun `GIVEN a recurrence with two dates WHEN resolve THEN assert date is displayed`() {
        val recurrence = Recurrences.Seasonal(
            listOf(
                DayOfYear(DayOfMonth(1), Month.April),
                DayOfYear(DayOfMonth(20), Month.September),
            ),
        )
        val textState = SeasonalRecurrencesTextState(recurrence)
        composeTestRule.setContent {
            val text = textState.resolve()
            assertEquals(
                "APR and SEP",
                text,
            )
        }
    }

    @Test
    fun `GIVEN a recurrence with three dates WHEN resolve THEN assert date is displayed`() {
        val recurrence = Recurrences.Seasonal(
            listOf(
                DayOfYear(DayOfMonth(1), Month.January),
                DayOfYear(DayOfMonth(20), Month.May),
                DayOfYear(DayOfMonth(31), Month.August),
            ),
        )
        val textState = SeasonalRecurrencesTextState(recurrence)
        composeTestRule.setContent {
            val text = textState.resolve()
            assertEquals(
                "JAN, MAY and AUG",
                text,
            )
        }
    }
}
