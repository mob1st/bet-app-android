package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.fixtures.categorySuggestion
import br.com.mob1st.features.finances.impl.domain.fixtures.typeRecurrenceToRecurrences
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategorySuggestionRepository
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StartBuilderStepUseCaseTest {
    private lateinit var useCase: StartBuilderStepUseCase
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var categorySuggestionRepository: CategorySuggestionRepository

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk()
        categorySuggestionRepository = mockk()
        useCase = StartBuilderStepUseCase(
            categoryRepository = categoryRepository,
            categorySuggestionRepository = categorySuggestionRepository,
        )
    }

    @Test
    fun `GIVEN categories already added WHEN invoke THEN do nothing`() = runTest {
        // Given
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        every {
            categoryRepository.countByIsExpenseAndRecurrencesType(
                step.isExpense,
                step.type,
            )
        } returns flowOf(1L)

        // When
        useCase(step)

        // Then
        verify(exactly = 0) { categorySuggestionRepository.getByStep(any()) }
        coVerify(exactly = 0) { categoryRepository.addAll(any()) }
    }

    @Test
    fun `GIVEN empty categories WHEN invoke THEN assert suggestions are added`() = runTest {
        // Given
        val step = Arb.bind<BudgetBuilderAction.Step>().next()
        val suggestions = Arb.categorySuggestion().chunked(1..3).next()
        val slot = slot<List<Category>>()
        every {
            categoryRepository.countByIsExpenseAndRecurrencesType(
                step.isExpense,
                step.type,
            )
        } returns flowOf(0L)
        every { categorySuggestionRepository.getByStep(step) } returns flowOf(suggestions)
        coEvery { categoryRepository.addAll(capture(slot)) } returns Unit

        // When
        useCase(step)
        val expected = suggestions.map { suggestion ->
            Category(
                isSuggested = true,
                name = suggestion.name,
                image = suggestion.image,
                recurrences = typeRecurrenceToRecurrences.getLeftValue(step.type),
                isExpense = step.isExpense,
            )
        }
        assertEquals(expected, slot.captured)
        // Then
        verify(exactly = 1) { categorySuggestionRepository.getByStep(step) }
        coVerify(exactly = 1) { categoryRepository.addAll(slot.captured) }
    }
}
