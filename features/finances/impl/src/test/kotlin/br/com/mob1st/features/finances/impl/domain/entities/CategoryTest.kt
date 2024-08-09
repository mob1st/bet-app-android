package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.features.finances.impl.domain.values.recurrences
import br.com.mob1st.features.finances.impl.domain.values.uri
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.arbs.products.products
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CategoryTest {
    @Test
    fun `GIVEN non default values WHEN create category THEN assert default values are expected`() {
        val recurrences = Arb.recurrences().next()
        val isExpense = Arb.boolean().next()
        val isSuggested = Arb.boolean().next()
        val name = Arb.products().map { it.name }.next()
        val image = Arb.uri().next()
        val actual = Category(
            name = name,
            image = image,
            isExpense = isExpense,
            isSuggested = isSuggested,
            recurrences = recurrences,
        )
        val expected = Category(
            id = Category.Id(0),
            amount = Money.Zero,
            name = name,
            image = image,
            isExpense = isExpense,
            isSuggested = isSuggested,
            recurrences = recurrences,
        )
        assertEquals(expected, actual)
    }
}
