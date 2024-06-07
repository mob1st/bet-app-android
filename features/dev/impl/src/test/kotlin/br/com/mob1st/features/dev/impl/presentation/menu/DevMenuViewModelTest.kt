package br.com.mob1st.features.dev.impl.presentation.menu

import app.cash.turbine.test
import br.com.mob1st.features.dev.impl.domain.DevMenu
import br.com.mob1st.features.dev.impl.domain.DevMenuEntry
import br.com.mob1st.features.dev.impl.domain.GetDevMenuUseCase
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.presentation.DevSettingsNavTarget
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.tests.featuresutils.ViewModelTestExtension
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(ViewModelTestExtension::class)
internal class DevMenuViewModelTest {
    private lateinit var getDevMenuUseCase: GetDevMenuUseCase
    private lateinit var stateHolder: DevMenuUiStateHolder

    @BeforeEach
    fun setUp() {
        getDevMenuUseCase = mockk()
        stateHolder = mockk()
    }

    @Test
    fun `GIVEN a long result WHEN get initial state THEN ui state should be empty`() = runTest {
        every { getDevMenuUseCase() } returns flow { }
        initViewModel().uiStateOutput.test {
            assertEquals(
                DevMenuUiState.Empty,
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a failure result WHEN get initial state THEN modal should be displayed`() = runTest {
        every { getDevMenuUseCase() } returns flow { throw Exception() }
        val viewModel = initViewModel()
        viewModel.dialogOutput.test {
            assertEquals(
                CommonError.Unknown,
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a dev menu WHEN get state THEN ui state should be loaded`() = runTest {
        val devMenu = DevMenu(
            backendEnvironment = BackendEnvironment.entries.random(),
            entries = DevMenuEntry.entries,
        )
        every { getDevMenuUseCase() } returns flow { emit(devMenu) }
        every { stateHolder.asUiState(devMenu) } returns DevMenuUiState.Loaded(persistentListOf())

        initViewModel().uiStateOutput.test {
            assertEquals(
                DevMenuUiState.Loaded(persistentListOf()),
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a dev menu WHEN select item THEN navigate to entry`() = runTest {
        // GIVEN
        val devMenu = DevMenu(
            backendEnvironment = BackendEnvironment.entries.random(),
            entries = DevMenuEntry.entries,
        )
        every { getDevMenuUseCase() } returns flow { emit(devMenu) }
        every { stateHolder.asUiState(devMenu) } returns DevMenuUiState.Loaded(persistentListOf())
        every { stateHolder.isImplemented(1) } returns true
        every { stateHolder.getNavTarget(1) } returns DevSettingsNavTarget.BackendEnvironment

        // WHEN
        val viewModel = initViewModel()
        viewModel.selectItem(1)

        // THEN
        viewModel.navigationOutput.test {
            assertEquals(
                DevSettingsNavTarget.BackendEnvironment,
                awaitItem(),
            )
        }
    }

    @Test
    fun `GIVEN a dev menu WHEN select an invalid item THEN show snack`() = runTest {
        val devMenu = DevMenu(
            backendEnvironment = BackendEnvironment.entries.random(),
            entries = DevMenuEntry.entries,
        )
        every { getDevMenuUseCase() } returns flow { emit(devMenu) }
        every { stateHolder.asUiState(devMenu) } returns DevMenuUiState.Loaded(persistentListOf())
        every { stateHolder.isImplemented(0) } returns false

        val viewModel = initViewModel()

        viewModel.selectItem(0)

        viewModel.snackbarOutput.test {
            assertEquals(
                DevMenuSnackbar.Todo,
                awaitItem(),
            )
        }
    }

    private fun initViewModel() = DevMenuViewModel(getDevMenuUseCase, stateHolder)
}
