package br.com.mob1st.features.finances.impl.domain.usecases

import app.cash.turbine.test
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.core.observability.events.ScreenViewEvent
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.events.BuilderStepScreenViewFactory
import br.com.mob1st.features.finances.impl.domain.fixtures.budgetBuilder
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.next
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetBudgetBuilderUseCaseTest {
    private lateinit var useCase: GetCategoryBuilderUseCase

    private lateinit var analyticsReporter: AnalyticsReporter
    private lateinit var categoryRepository: CategoriesRepository
    private lateinit var builderStepScreenViewFactory: BuilderStepScreenViewFactory
    private lateinit var builderFactory: BudgetBuilder.Factory

    @BeforeEach
    fun setUp() {
        analyticsReporter = mockk(relaxed = true)
        categoryRepository = mockk()
        builderStepScreenViewFactory = mockk()
        builderFactory = mockk()

        useCase = GetCategoryBuilderUseCase(
            analyticsReporter = analyticsReporter,
            categoryRepository = categoryRepository,
            screenViewFactory = builderStepScreenViewFactory,
            builderFactory = builderFactory,
        )
    }

    @Test
    fun `GIVEN multiple list of suggestions And categories WHEN get category builder THEN verify screen view event is logged only once`() = runTest {
        // Given
        val categoriesFlow = MutableSharedFlow<List<Category>>()
        val step = Arb.bind<BuilderNextAction.Step>().next()

        every {
            categoryRepository.getByIsExpenseAndRecurrencesType(
                isExpense = step.isExpense,
                recurrenceType = step.type,
            )
        } returns categoriesFlow
        every { builderFactory.create(eq(step), any()) } answers {
            Arb.budgetBuilder().next()
        }
        every { builderStepScreenViewFactory.create(step) } answers {
            Arb.bind<ScreenViewEvent>().next()
        }

        // When
        useCase[step].test {
            categoriesFlow.emit(emptyList())
            awaitItem()
            categoriesFlow.emit(Arb.category().chunked(3..5).next())
            awaitItem()
            categoriesFlow.emit(Arb.category().chunked(3..5).next())
            awaitItem()
            // Then
            verify(exactly = 1) { analyticsReporter.log(any()) }
            verify(exactly = 3) { builderFactory.create(eq(step), any()) }
        }
    }
}
