package br.com.mob1st.features.finances.impl.domain.usecases

import app.cash.turbine.test
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.core.observability.events.ScreenViewEvent
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.events.BuilderStepScreenViewFactory
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.utils.moduleFixture
import com.appmattus.kotlinfixture.Fixture
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
    private lateinit var fixture: Fixture

    @BeforeEach
    fun setUp() {
        analyticsReporter = mockk(relaxed = true)
        categoryRepository = mockk()
        builderStepScreenViewFactory = mockk()
        fixture = moduleFixture

        useCase = GetCategoryBuilderUseCase(
            analyticsReporter = analyticsReporter,
            categoryRepository = categoryRepository,
            builderStepScreenViewFactory = builderStepScreenViewFactory,
        )
    }

    @Test
    fun `GIVEN multiple list of suggestions And categories WHEN get category builder THEN verify screen view event is logged only once`() = runTest {
        // Given
        val categoriesFlow = MutableSharedFlow<List<Category>>()
        val suggestionsFlow = MutableSharedFlow<List<CategorySuggestion>>()
        val step = fixture<BuilderNextAction.Step>()
        val screenViewEvent = fixture<ScreenViewEvent>()

        every { categoryRepository.getByStep(step) } returns categoriesFlow
        every { builderStepScreenViewFactory.create(step) } returns screenViewEvent

        // When
        useCase[step].test {
            categoriesFlow.emit(emptyList())
            suggestionsFlow.emit(listOf(fixture()))
            awaitItem()
            categoriesFlow.emit(listOf(fixture()))
            awaitItem()
            suggestionsFlow.emit(listOf(fixture()))
            awaitItem()
            // Then
            verify(exactly = 1) { analyticsReporter.log(screenViewEvent) }
        }
    }
}
