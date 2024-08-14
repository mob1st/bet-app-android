package br.com.mob1st.features.finances.impl.ui.category.navigation

import br.com.mob1st.features.finances.impl.domain.entities.CategoryDefaultValues
import br.com.mob1st.features.finances.impl.domain.values.category
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CategoryDetailArgsTest {
    @Test
    fun `GIVEN a category WHEN create args THEN assert it is edit`() {
        // Given
        val category = Arb.category().next()

        // When
        val actual = CategoryDetailArgs(category)

        // Then
        val expected = CategoryDetailArgs(
            intent = CategoryDetailArgs.Intent.Edit(category.id.value),
            name = category.name,
            isExpense = category.isExpense,
            recurrenceType = category.recurrences.asType(),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN a name and default values WHEN create args THEN assert it is create`() {
        // Given
        val name = Arb.string().next()
        val defaultValues = Arb.bind<CategoryDefaultValues>().next()

        // When
        val actual = CategoryDetailArgs(name, defaultValues)

        // Then
        val expected = CategoryDetailArgs(
            intent = CategoryDetailArgs.Intent.Create,
            name = name,
            isExpense = defaultValues.isExpense,
            recurrenceType = defaultValues.recurrenceType,
        )
        assertEquals(expected, actual)
    }
}
