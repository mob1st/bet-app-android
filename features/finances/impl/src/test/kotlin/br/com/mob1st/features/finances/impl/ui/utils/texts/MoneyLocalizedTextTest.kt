package br.com.mob1st.features.finances.impl.ui.utils.texts

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mob1st.core.kotlinx.structures.Money
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoneyLocalizedTextTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN an amount WHEN format the text THEN assert result`() {
        val moneyState = MoneyLocalizedText(Money(1000))
        composeTestRule.setContent {
            val text = moneyState.resolve()
            assertEquals(
                "$10.00",
                text,
            )
        }
    }
}
