package br.com.mob1st.features.finances.impl.domain.usecases

import br.com.mob1st.core.observability.events.AnalyticsEvent
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.positiveInt
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    @Test
    fun `GIVEN a builder with no remaining inputs And next action is a step WHEN proceed THEN assert next step is started`() = runTest {
        val arbId = Arb.bind<BuilderNextAction.Step>().next()
        val arbNext = Arb.bind<BuilderNextAction.Step>().next()
        val builder = mockk<BudgetBuilder> {
            every { id } returns arbId
            every { calculateRemainingInputs() } returns 0
            every { next } returns arbNext
        }
        useCase(builder)
        coVerify { startBuilderStepUseCase(arbNext) }
        verify(exactly = 0) { analyticsReporter.report(any()) }
    }

    @Test
    fun `GIVEN a builder with no remaining inputs And next action is Complete WHEN proceed THEN assert next step is not started`() = runTest {
        val arbId = Arb.bind<BuilderNextAction.Step>().next()
        val builder = mockk<BudgetBuilder> {
            every { id } returns arbId
            every { calculateRemainingInputs() } returns 0
            every { next } returns BuilderNextAction.Complete
        }
        useCase(builder)
        coVerify(exactly = 0) { startBuilderStepUseCase(any()) }
        verify(exactly = 0) { analyticsReporter.report(any()) }
    }

    @Test
    fun `GIVEN a builder with remaining inputs WHEN proceed THEN assert event is logged and error is thrown`() = runTest {
        val arbId = Arb.bind<BuilderNextAction.Step>().next()
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
}
