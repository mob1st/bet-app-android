package br.com.mob1st.features.dev.impl.presentation.menu

import app.cash.turbine.test
import br.com.mob1st.features.dev.impl.domain.DevMenu
import br.com.mob1st.features.dev.impl.domain.DevMenuEntry
import br.com.mob1st.features.dev.impl.domain.GetDevMenuUseCase
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.utils.errors.CommonError
import br.com.mob1st.tests.featuresutils.ViewModelTestExtension
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(ViewModelTestExtension::class)
internal class DevMenuViewModelTest {
    private lateinit var getDevMenuUseCase: GetDevMenuUseCase

    @BeforeEach
    fun setUp() {
        getDevMenuUseCase = mockk()
    }

    @Test
    fun `GIVEN a long result WHEN get initial state THEN ui state should be empty`() =
        runTest {
            coEvery { getDevMenuUseCase() } returns flow { }
            DevMenuViewModel(getDevMenuUseCase).uiOutput.test {
                assertEquals(
                    DevMenuUiState.Empty,
                    awaitItem(),
                )
            }
        }

    @Test
    fun `GIVEN a failure result WHEN get initial state THEN modal should be displayed`() =
        runTest {
            coEvery { getDevMenuUseCase() } returns flow { throw Exception() }
            val viewModel = DevMenuViewModel(getDevMenuUseCase)
            viewModel.modalOutput.test {
                assertEquals(
                    CommonError.Unknown.modal(false),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `GIVEN a dev menu WHEN get state THEN ui state should be loaded`() =
        runTest {
            val devMenu =
                DevMenu(
                    backendEnvironment = BackendEnvironment.entries.random(),
                    entries = DevMenuEntry.entries,
                )
            coEvery { getDevMenuUseCase() } returns flow { emit(devMenu) }
            DevMenuViewModel(getDevMenuUseCase).uiOutput.test {
                assertEquals(
                    DevMenuUiState.Loaded(devMenu),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `GIVEN a dev menu WHEN select item THEN navigate to entry`() =
        runTest {
            val devMenu =
                DevMenu(
                    backendEnvironment = BackendEnvironment.entries.random(),
                    entries = DevMenuEntry.entries,
                )
            coEvery { getDevMenuUseCase() } returns flow { emit(devMenu) }
            val viewModel = DevMenuViewModel(getDevMenuUseCase)

            viewModel.selectItem(1)

            viewModel.navigationOutput.test {
                assertEquals(
                    DevMenuNavigable(DevMenuEntry.Gallery),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `GIVEN a dev menu WHEN select an invalid item THEN show snack`() =
        runTest {
            val devMenu =
                DevMenu(
                    backendEnvironment = BackendEnvironment.entries.random(),
                    entries = DevMenuEntry.entries,
                )
            coEvery { getDevMenuUseCase() } returns flow { emit(devMenu) }
            val viewModel = DevMenuViewModel(getDevMenuUseCase)

            viewModel.selectItem(0)

            viewModel.snackbarOutput.test {
                assertEquals(
                    DevMenuUiState.TodoSnack(),
                    awaitItem(),
                )
            }
        }
}
