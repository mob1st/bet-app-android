package br.com.mob1st.features.finances.impl.infra.data.repositories.categories

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.infra.data.fixtures.categories
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CategoriesDataMapTest {
    @Test
    fun `GIVEN a recurrence mapper WHEN map to domain THEN assert properties are mapped`() {
        val categories = Arb.categories().next()
        val actual = CategoriesDataMap.invoke(categories)
        val expected = Category(
            id = Category.Id(categories.id),
            name = categories.name,
            image = Uri(categories.image),
            amount = Money(categories.amount),
            isExpense = categories.is_expense,
            isSuggested = categories.is_suggested,
            recurrences = Recurrences.Variable,
        )
        assertEquals(
            expected,
            actual,
        )
    }
}
