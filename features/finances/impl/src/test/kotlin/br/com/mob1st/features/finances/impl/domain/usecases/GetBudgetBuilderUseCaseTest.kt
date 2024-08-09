package br.com.mob1st.features.finances.impl.domain.usecases

import app.cash.turbine.test
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.values.category
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.next
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetBudgetBuilderUseCaseTest {
    private lateinit var useCase: GetBudgetBuilderForStepUseCase
    private lateinit var categoryRepository: CategoriesRepository

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk()

        useCase = GetBudgetBuilderForStepUseCase(
            categoryRepository = categoryRepository,
        )
    }

    @Test
    fun `GIVEN multiple list of suggestions And categories WHEN get category builder THEN verify screen view event is logged only once`() = runTest {
        // Given
        val categoriesFlow = MutableSharedFlow<List<Category>>()
        val step = Arb.bind<BudgetBuilderAction.Step>().next()

        every {
            categoryRepository.getByIsExpenseAndRecurrencesType(
                isExpense = step.isExpense,
                recurrenceType = step.type,
            )
        } returns categoriesFlow

        // When
        useCase[step].test {
            categoriesFlow.emit(emptyList())
            assertEquals(
                BudgetBuilder(step, emptyList()),
                awaitItem(),
            )
            val list = Arb.category().chunked(3..5).next()
            categoriesFlow.emit(list)
            assertEquals(
                BudgetBuilder(step, list),
                awaitItem(),
            )
        }
    }
}
