package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.ScreenViewEvent
import br.com.mob1st.features.finances.impl.domain.entities.RecurrentCategoryGroup
import br.com.mob1st.features.finances.impl.fakes.FakeRecurrenceBuilderRepository
import br.com.mob1st.tests.featuresutils.FakeAnalyticsReporter
import br.com.mob1st.tests.featuresutils.fixture
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetFixedExpensesUseCaseTest {

    private lateinit var subject: GetFixedExpensesUseCaseImpl

    private lateinit var recurrenceBuilderRepository: FakeRecurrenceBuilderRepository
    private lateinit var analyticsReporter: FakeAnalyticsReporter

    @BeforeEach
    fun setUp() {
        recurrenceBuilderRepository = FakeRecurrenceBuilderRepository()
        analyticsReporter = FakeAnalyticsReporter()
        subject = GetFixedExpensesUseCaseImpl(
            recurrenceBuilderRepository = recurrenceBuilderRepository,
            analyticsReporter = analyticsReporter
        )
    }

    @Test
    fun `GIVEN a builder WHEN get THEN return the list of fixed expenses`() = runTest {
        // GIVEN
        recurrenceBuilderRepository.setState.value = fixture()
        val expected =
            RecurrentCategoryGroup(
                recurrenceBuilderRepository.get().first().fixedExpensesStep.list
            )

        // WHEN
        val actual = subject().first()

        // THEN
        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun `GIVEN a builder WHEN get THEN log the screen view event`() = runTest {
        // GIVEN
        recurrenceBuilderRepository.setState.value = fixture()

        // WHEN
        subject().first()

        // THEN
        assertEquals(
            ScreenViewEvent("fin_builder_fixed_expenses"),
            analyticsReporter.logState.first()
        )
    }
}
