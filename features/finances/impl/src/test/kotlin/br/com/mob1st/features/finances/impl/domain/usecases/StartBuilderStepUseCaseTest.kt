package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.CategorySuggestionRepository
import br.com.mob1st.features.finances.impl.domain.values.category
import br.com.mob1st.features.finances.impl.domain.values.categorySuggestion
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StartBuilderStepUseCaseTest {
    private lateinit var useCase: StartBuilderStepUseCase
    private lateinit var categoriesRepository: CategoriesRepository
    private lateinit var categorySuggestionRepository: CategorySuggestionRepository
    private lateinit var categoryFactory: Category.Factory

    @BeforeEach
    fun setUp() {
        categoriesRepository = mockk()
        categorySuggestionRepository = mockk()
        categoryFactory = mockk()
        useCase = StartBuilderStepUseCase(
            categoriesRepository = categoriesRepository,
            categorySuggestionRepository = categorySuggestionRepository,
            categoryFactory = categoryFactory,
        )
    }

    @Test
    fun `GIVEN categories already added WHEN invoke THEN do nothing`() = runTest {
        // Given
        val step = Arb.bind<BuilderNextAction.Step>().next()
        every {
            categoriesRepository.countByIsExpenseAndRecurrencesType(
                step.isExpense,
                step.type,
            )
        } returns 1L

        // When
        useCase(step)

        // Then
        verify(exactly = 0) { categorySuggestionRepository.getByStep(any()) }
        coVerify(exactly = 0) { categoriesRepository.addAll(any()) }
    }

    @Test
    fun `GIVEN empty categories WHEN invoke THEN assert suggestions are added`() = runTest {
        // Given
        val step = Arb.bind<BuilderNextAction.Step>().next()
        val suggestions = Arb.categorySuggestion().chunked(1..3).next()

        every {
            categoriesRepository.countByIsExpenseAndRecurrencesType(
                step.isExpense,
                step.type,
            )
        } returns 0L
        every { categorySuggestionRepository.getByStep(step) } returns flowOf(suggestions)
        every { categoryFactory.create(eq(step), any()) } answers {
            Arb.category().next()
        }
        coEvery { categoriesRepository.addAll(any()) } returns Unit

        // When
        useCase(step)

        // Then
        verify(exactly = 1) { categorySuggestionRepository.getByStep(step) }
        verify(exactly = suggestions.size) { categoryFactory.create(eq(step), any()) }
        coVerify(exactly = 1) { categoriesRepository.addAll(any()) }
    }
}
