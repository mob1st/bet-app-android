package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilderAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.SeasonalExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.domain.fixtures.budgetBuilder
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProceedBuilderUseCaseTest {
    private lateinit var useCase: ProceedBuilderUseCase
    private lateinit var startBuilderStepUseCase: StartBuilderStepUseCase
    private lateinit var analyticsReporter: AnalyticsReporter

    @BeforeEach
    fun setUp() {
        startBuilderStepUseCase = mockk(relaxed = true)
        analyticsReporter = mockk(relaxed = true)
        useCase = ProceedBuilderUseCase(
            startBuilderStepUseCase = startBuilderStepUseCase,
            analyticsReporter = analyticsReporter,
        )
    }

    @ParameterizedTest
    @MethodSource("stepsSource")
    fun `GIVEN a builder with no remaining inputs And next action is a step WHEN proceed THEN assert next step is started`(
        currentStep: BudgetBuilderAction.Step,
        expectedStep: BudgetBuilderAction.Step,
    ) = runTest {
        val builder = Arb.budgetBuilder().map {
            it.copy(id = currentStep)
        }.next()
        val actual = useCase(builder)
        coVerify { startBuilderStepUseCase(expectedStep) }
        verify(exactly = 0) { analyticsReporter.report(any()) }
        assertEquals(expectedStep, actual)
    }

    @Test
    fun `GIVEN a builder with no remaining inputs And next action is Complete WHEN proceed THEN assert next step is not started`() = runTest {
        val builder = Arb.budgetBuilder().map {
            it.copy(id = FixedIncomesStep)
        }.next()
        val actual = useCase(builder)
        coVerify(exactly = 0) { startBuilderStepUseCase(any()) }
        verify(exactly = 0) { analyticsReporter.report(any()) }
        assertEquals(BudgetBuilderAction.Complete, actual)
    }

    @Test
    fun `GIVEN a builder with remaining inputs WHEN proceed THEN assert event is logged and error is thrown`() = runTest {
        val arbId = Arb.bind<BudgetBuilderAction.Step>().next()
        val remainingInputs = Arb.positiveInt().next()
        val builder = mockk<BudgetBuilder> {
            every { id } returns arbId
            every { calculateRemainingInputs() } returns remainingInputs
        }
        val exception = assertFailsWith<ProceedBuilderUseCase.NotEnoughInputsException> {
            useCase(builder)
        }
        verify {
            analyticsReporter.report(
                AnalyticsEvent(
                    name = "not_enough_items_to_complete",
                    params = mapOf(
                        "step" to arbId,
                        "remainingItems" to remainingInputs,
                    ),
                ),
            )
        }
        assertEquals(remainingInputs, exception.remainingInputs)
    }

    companion object {
        @JvmStatic
        fun stepsSource() = listOf(
            arguments(
                VariableExpensesStep,
                FixedExpensesStep,
            ),
            arguments(
                FixedExpensesStep,
                SeasonalExpensesStep,
            ),
            arguments(
                SeasonalExpensesStep,
                FixedIncomesStep,
            ),
        )
    }
}
