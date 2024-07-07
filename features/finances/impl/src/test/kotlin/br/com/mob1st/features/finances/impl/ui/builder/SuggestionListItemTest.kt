package br.com.mob1st.features.finances.impl.ui.builder

import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.ui.builder.steps.SuggestionListItem
import br.com.mob1st.features.finances.impl.ui.utils.texts.MoneyTextState
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SuggestionListItemTest {
    @Test
    fun `GIVEN a suggestion And no linked category WHEN get the leading THEN assert it's correct`() {
        // Given
        val nameResId = moduleFixture<Int>()
        val listItem = SuggestionListItem(
            suggestion = CategorySuggestion(
                id = CategorySuggestion.Id(),
                nameResId = nameResId,
                linkedCategory = null,
            ),
        )
        val actual = listItem.headline
        assertEquals(
            TextState(nameResId),
            actual,
        )
    }

    @Test
    fun `GIVEN a suggestion And a linked category WHEN get the leading THEN assert it's the category name`() {
        val suggestion = CategorySuggestion(
            id = CategorySuggestion.Id(),
            nameResId = moduleFixture(),
            linkedCategory = moduleFixture<Category>().copy(
                name = "Category Name",
            ),
        )
        val listItem = SuggestionListItem(suggestion)
        val actual = listItem.headline
        assertEquals(
            TextState("Category Name"),
            actual,
        )
    }

    @Test
    fun `GIVEN a suggestion And no linked category WHEN get the value THEN assert it's null`() {
        val listItem = SuggestionListItem(
            suggestion = CategorySuggestion(
                id = CategorySuggestion.Id(),
                nameResId = moduleFixture(),
                linkedCategory = null,
            ),
        )
        val actual = listItem.value
        assertNull(actual)
    }

    @Test
    fun `GIVEN a suggestion And a linked category WHEN get the value THEN assert it's the category amount`() {
        val suggestion = CategorySuggestion(
            id = CategorySuggestion.Id(),
            nameResId = moduleFixture(),
            linkedCategory = moduleFixture<Category>().copy(
                amount = Money(4000),
            ),
        )
        val listItem = SuggestionListItem(suggestion)
        val actual = listItem.value
        assertEquals(
            MoneyTextState(Money(4000)),
            actual,
        )
    }
}
