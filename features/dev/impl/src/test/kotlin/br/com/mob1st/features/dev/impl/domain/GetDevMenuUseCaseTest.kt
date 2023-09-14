package br.com.mob1st.features.dev.impl.domain

import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettings
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettingsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetDevMenuUseCaseTest {

    private lateinit var projectSettingsRepository: ProjectSettingsRepository

    @BeforeEach
    fun setUp() {
        projectSettingsRepository = mockk()
    }

    @Test
    fun `GIVEN a backend repository WHEN invoke THEN return dev menu `() = runTest {
        val projectSettings = ProjectSettings(
            backendEnvironment = BackendEnvironment.values().random(),
            buildInfo = mockk()
        )
        givenProjectSettings(flowOf(projectSettings))
        val useCase = GetDevMenuUseCase(projectSettingsRepository)
        val actual = useCase().first()
        assertEquals(
            expected = DevMenu(projectSettings.backendEnvironment),
            actual = actual
        )
    }

    private fun givenProjectSettings(expected: Flow<ProjectSettings>) {
        every { projectSettingsRepository.get() } returns expected
    }
}
