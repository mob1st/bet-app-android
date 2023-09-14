package br.com.mob1st.features.dev.impl.presentation.menu

import br.com.mob1st.features.dev.impl.domain.DevMenu
import br.com.mob1st.features.dev.impl.domain.GetDevMenuUseCase
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.tests.featuresutils.FakeQueueSnackDismissManager
import br.com.mob1st.tests.featuresutils.ViewModelTestExtension
import br.com.mob1st.tests.unit.assertStateOnCollect
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(ViewModelTestExtension::class)
internal class DevMenuViewModelTest {

    private lateinit var getDevMenuUseCase: GetDevMenuUseCase
    private lateinit var snackDismissManager: FakeQueueSnackDismissManager

    @BeforeEach
    fun setUp() {
        getDevMenuUseCase = mockk()
        snackDismissManager = FakeQueueSnackDismissManager()
    }

    @ParameterizedTest
    @ArgumentsSource(InitialStateArguments::class)
    fun `GIVEN a dev menu WHEN collect THEN assert the expected state`(
        devMenuFlow: Flow<DevMenu>,
        expectedState: DevMenuPageState,
    ) = runTest {
        givenDevMenu(devMenuFlow)
        val viewModel = initViewModel()
        assertStateOnCollect(viewModel.output, expectedState)
    }

    @Test
    fun `GIVEN a dev menu And a valid option WHEN select list item THEN trigger navigation`() = runTest {
        val backendEnvironment = BackendEnvironment.values().random()
        val devMenu = spyk(DevMenu(backendEnvironment))
        every { devMenu.isAllowed(any()) } returns true
        givenDevMenu(flowOf(devMenu))
        val viewModel = initViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.output.collect()
        }

        val position = (0..devMenu.entries.lastIndex).random()
        viewModel.selectItem(position)

        assertEquals(
            DevMenuPageState.Loaded(
                menu = DevMenu(backendEnvironment),
                selectedItem = position
            ),
            viewModel.output.value
        )
    }

    @Test
    fun `GIVEN an invalid position WHEN select list item THEN show snackbar`() = runTest {
        val backendEnvironment = BackendEnvironment.values().random()
        val devMenu = spyk(DevMenu(backendEnvironment))
        every { devMenu.isAllowed(any()) } returns false
        givenDevMenu(flowOf(devMenu))
        val viewModel = initViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.output.collect()
        }

        val position = (0..devMenu.entries.lastIndex).random()
        viewModel.selectItem(position)

        assertEquals(
            DevMenuPageState.Loaded(
                menu = DevMenu(backendEnvironment),
                snack = DevMenuPageState.TodoSnack()
            ),
            viewModel.output.value
        )
    }

    @Test
    fun `GIVEN a navigation event WHEN consume THEN remove navigation from state`() = runTest {
        val backendEnvironment = BackendEnvironment.values().random()
        val devMenu = spyk(DevMenu(backendEnvironment))
        every { devMenu.isAllowed(any()) } returns true

        givenDevMenu(flowOf(devMenu))
        val viewModel = initViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.output.collect()
        }

        viewModel.selectItem(1)
        viewModel.consumeNavigation()

        assertEquals(
            DevMenuPageState.Loaded(devMenu),
            viewModel.output.value
        )
    }

    private fun givenDevMenu(expected: Flow<DevMenu>) {
        every { getDevMenuUseCase() } returns expected
    }

    private fun initViewModel() = DevMenuViewModel(
        getMenuUseCase = getDevMenuUseCase,
        snackManager = snackDismissManager
    )

    object InitialStateArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val backendEnvironment = BackendEnvironment.values().random()
            return Stream.of(
                Arguments.of(
                    emptyFlow<DevMenu>(),
                    DevMenuPageState.Empty
                ),
                Arguments.of(
                    flow<DevMenu> { error("Unknown") },
                    DevMenuPageState.Failed(commonError = CommonError.Unknown)
                ),
                Arguments.of(
                    flowOf(DevMenu(backendEnvironment)),
                    DevMenuPageState.Loaded(DevMenu(backendEnvironment))
                )
            )
        }
    }
}
