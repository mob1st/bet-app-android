package br.com.mob1st.features.finances.impl.ui.builder.intro

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import br.com.mob1st.features.finances.impl.domain.entities.BudgetBuilder
import br.com.mob1st.features.finances.impl.domain.usecases.StartBuilderStepUseCase
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.tests.featuresutils.MainDispatcherTestExtension
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MainDispatcherTestExtension::class)
internal class BuilderIntroViewModelTest {
    private lateinit var startBuilderStep: StartBuilderStepUseCase
    private lateinit var router: BuilderRouter

    @BeforeEach
    fun setUp() {
        startBuilderStep = mockk(relaxed = true)
        router = mockk()
    }

    @Test
    fun `WHEN get initial state THEN assert loading is false`() = runTest {
        val viewModel = initViewModel()
        turbineScope {
            val receiveUiState = viewModel.uiState.testIn(backgroundScope)
            val receiveConsumables = viewModel.consumableUiState.testIn(backgroundScope)
            assertEquals(
                BuilderIntroUiState(false),
                receiveUiState.awaitItem(),
            )
            assertEquals(
                BuilderIntroConsumables(),
                receiveConsumables.awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a long step start WHEN start THEN assert loading is displayed`() = runTest {
        val completedDeferred = CompletableDeferred<Unit>()
        coEvery { startBuilderStep(any()) } coAnswers {
            completedDeferred.await()
        }
        val viewModel = initViewModel()
        viewModel.start()
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isLoading)
        }
    }

    @Test
    fun `GIVEN a failure to step start WHEN start THEN assert error is displayed And loading is hidden`() = runTest {
        coEvery { startBuilderStep(any()) } throws Exception()
        val viewModel = initViewModel()
        viewModel.start()
        turbineScope {
            val receiveUiState = viewModel.uiState.testIn(backgroundScope)
            val receiveConsumables = viewModel.consumableUiState.testIn(backgroundScope)
            assertEquals(
                BuilderIntroUiState(false),
                receiveUiState.awaitItem(),
            )
            assertEquals(
                BuilderIntroConsumables(error = CommonError.Unknown),
                receiveConsumables.awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a initial action WHEN start THEN assert first step is used And router send it`() = runTest {
        val step = BudgetBuilder.firstStep()
        val route = Arb.bind<BuilderRoute>().next()
        every { router.send(step) } returns route
        val viewModel = initViewModel()
        viewModel.start()
        turbineScope {
            val receiveUiState = viewModel.uiState.testIn(backgroundScope)
            val receiveConsumables = viewModel.consumableUiState.testIn(backgroundScope)
            assertEquals(
                BuilderIntroUiState(false),
                receiveUiState.awaitItem(),
            )
            assertEquals(
                BuilderIntroConsumables(route = route),
                receiveConsumables.awaitItem(),
            )
        }
    }

    private fun initViewModel() = BuilderIntroViewModel(
        startBuilderStep = startBuilderStep,
        router = router,
    )
}
