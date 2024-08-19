package br.com.mob1st.features.finances.impl.ui.category.detail

import br.com.mob1st.features.finances.impl.domain.fixtures.category
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CategoryEntryTest {
    @Test
    fun `GIVEN a category WHEN create entry THEN assert category properties are used`() {
        val category = Arb.category().next()
        val actual = CategoryEntry(category)
        val expected = CategoryEntry(
            name = category.name,
            image = category.image,
            recurrences = category.recurrences,
            amount = category.amount,
        )
        assertEquals(
            expected,
            actual,
        )
    }
}
