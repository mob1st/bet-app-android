package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.observability.events.ScreenViewEvent
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceBuilder
import br.com.mob1st.features.finances.impl.fakes.FakeRecurrenceBuilderRepository
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItemGroup
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentTransaction
import br.com.mob1st.tests.featuresutils.FakeAnalyticsReporter
import br.com.mob1st.tests.featuresutils.fixture
import kotlinx.collections.immutable.toPersistentList
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
        val fixture = fixture<RecurrentCategory.Fixed>().copy(
            recurrentTransaction = fixture<RecurrentTransaction>().copy(
                amount = Money(100),
                type = BudgetItem.Type.EXPENSE
            )
        )
        val expected = BudgetItemGroup(
            items = listOf(
                BudgetItemGroup.ProportionalItem(
                    item = fixture,
                    proportion = 100
                )
            ),
            summaries = BudgetItemGroup.Summaries(
                incomes = Money.Zero,
                expenses = Money(100),
                balance = Money(-100)
            )
        )
        recurrenceBuilderRepository.setState.value = fixture<RecurrenceBuilder>().copy(
            fixedExpensesStep = RecurrenceBuilder.Step(
                list = listOf(fixture).toPersistentList(),
                isCompleted = false
            )
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
