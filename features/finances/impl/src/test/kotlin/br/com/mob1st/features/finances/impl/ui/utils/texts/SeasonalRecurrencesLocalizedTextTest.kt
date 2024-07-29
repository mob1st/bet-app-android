package br.com.mob1st.features.finances.impl.ui.utils.texts

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class SeasonalRecurrencesLocalizedTextTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN a recurrence with one date WHEN resolve THEN assert date is displayed`() {
        val recurrence = Recurrences.Seasonal(
            listOf(DayOfYear(1)),
        )
        val textState = SeasonalRecurrencesLocalizedText(recurrence)
        composeTestRule.setContent {
            val text = textState.resolve()
            assertEquals(
                "JAN",
                text,
            )
        }
    }

    @Test
    fun `GIVEN a recurrence with two dates WHEN resolve THEN assert date is displayed`() {
        val recurrence = Recurrences.Seasonal(
            listOf(
                DayOfYear(1),
                DayOfYear(32),
            ),
        )
        val textState = SeasonalRecurrencesLocalizedText(recurrence)
        composeTestRule.setContent {
            val text = textState.resolve()
            assertEquals(
                "JAN and FEB",
                text,
            )
        }
    }

    @Test
    fun `GIVEN a recurrence with three dates WHEN resolve THEN assert date is displayed`() {
        val recurrence = Recurrences.Seasonal(
            listOf(
                DayOfYear(
                    1,
                ),
                DayOfYear(32),
                DayOfYear(60),
            ),
        )
        val textState = SeasonalRecurrencesLocalizedText(recurrence)
        composeTestRule.setContent {
            val text = textState.resolve()
            assertEquals(
                "JAN, FEB and MAR",
                text,
            )
        }
    }

    @Test
    fun `GIVEN no seasonal recurrences WHEN resolve THEN assert date is empty`() {
        val recurrence = Recurrences.Seasonal(emptyList())
        val textState = SeasonalRecurrencesLocalizedText(recurrence)
        composeTestRule.setContent {
            val text = textState.resolve()
            assertEquals(
                "",
                text,
            )
        }
    }
}
