package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.events.SetCategoryEventFactory
import br.com.mob1st.features.finances.impl.domain.fixtures.category
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SetCategoryUseCaseTest {
    private lateinit var useCase: SetCategoryUseCase
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var analyticsReporter: AnalyticsReporter
    private lateinit var eventFactory: SetCategoryEventFactory

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk(relaxed = true)
        analyticsReporter = mockk(relaxed = true)
        eventFactory = mockk()
        every { eventFactory.create(any()) } returns AnalyticsEvent(name = "fake")
        useCase = SetCategoryUseCase(
            categoryRepository = categoryRepository,
            analyticsReporter = analyticsReporter,
            eventFactory = eventFactory,
        )
    }

    @Test
    fun `GIVEN an existing category WHEN set THEN assert it is updated And event is sent`() = runTest {
        val existingCategory = Arb.category().next().copy(
            id = Category.Id(1),
        )
        useCase(existingCategory)
        coVerify(exactly = 1) {
            categoryRepository.set(existingCategory)
            eventFactory.create(existingCategory)
            analyticsReporter.report(AnalyticsEvent(name = "fake"))
        }

        coVerify(exactly = 0) {
            categoryRepository.add(any())
        }
    }

    @Test
    fun `GIVEN a new category WHEN set THEN assert it is added And event is sent`() = runTest {
        val newCategory = Arb.category().next().copy(
            id = Category.Id(),
        )
        useCase(newCategory)
        coVerify(exactly = 1) {
            categoryRepository.add(newCategory)
            eventFactory.create(newCategory)
            analyticsReporter.report(AnalyticsEvent(name = "fake"))
        }

        coVerify(exactly = 0) {
            categoryRepository.set(any())
        }
    }

    @Test
    fun `GIVEN a failure WHEN set new category THEN assert error is thrown And event is not sent`() = runTest {
        coEvery { categoryRepository.add(any()) } throws Exception()
        assertThrows<Exception> {
            useCase(Arb.category().next().copy(id = Category.Id()))
        }
        verify(exactly = 0) {
            eventFactory.create(any())
            analyticsReporter.report(any())
        }
    }

    @Test
    fun `GIVEN a failure WHEN set existing category THEN assert error is thrown And event is not sent`() = runTest {
        coEvery { categoryRepository.set(any()) } throws Exception()
        assertThrows<Exception> {
            useCase(Arb.category().next().copy(id = Category.Id(1)))
        }
        verify(exactly = 0) {
            eventFactory.create(any())
            analyticsReporter.report(any())
        }
    }
}
