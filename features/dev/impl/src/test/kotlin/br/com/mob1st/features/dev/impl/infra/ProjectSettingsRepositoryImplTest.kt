package br.com.mob1st.features.dev.impl.infra

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.domain.BuildInfo
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettings
import br.com.mob1st.tests.featuresutils.fixture
import br.com.mob1st.tests.featuresutils.randomEnum
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectSettingsRepositoryImplTest {
    private lateinit var backendEnvironmentDataSource: BackendEnvironmentDataSource
    private lateinit var buildInfoDataSource: BuildInfoDataSource

    @BeforeEach
    fun setUp() {
        backendEnvironmentDataSource = mockk()
        buildInfoDataSource = mockk()
    }

    @Test
    fun `GIVEN a build info And a backend environment WHEN get THEN assert the result`() =
        runTest {
            val expected =
                ProjectSettings(
                    buildInfo = fixture(),
                    backendEnvironment = randomEnum(),
                )
            givenBuildInfo(expected.buildInfo)
            givenBackendEnvironment(expected.backendEnvironment.name)
            val repo = initRepo(UnconfinedTestDispatcher())

            val actual = repo.get().first()
            assertEquals(
                expected =
                    ProjectSettings(
                        buildInfo = buildInfoDataSource.data,
                        backendEnvironment = BackendEnvironment.PRODUCTION,
                    ),
                actual = actual,
            )
        }

    @ParameterizedTest
    @ArgumentsSource(InvalidBackendEnvironmentArguments::class)
    fun `GIVEN a build type And an invalid backend environment WHEN get THEN assert expected fallback`(
        isReleaseBuild: Boolean,
        currentBackendEnvironment: String?,
        expectedFallback: BackendEnvironment,
    ) = runTest {
        val buildInfo = fixture<BuildInfo>().copy(isReleaseBuild = isReleaseBuild)
        givenBuildInfo(buildInfo)
        givenBackendEnvironment(currentBackendEnvironment)
        val repo = initRepo(UnconfinedTestDispatcher())

        val actual = repo.get().first()

        assertEquals(
            expected =
                ProjectSettings(
                    buildInfo = buildInfo,
                    backendEnvironment = expectedFallback,
                ),
            actual = actual,
        )
    }

    @ParameterizedTest
    @ArgumentsSource(SetBackendEnvironmentArguments::class)
    fun `GIVEN a new backend environment WHEN set THEN verify cache call`(
        input: BackendEnvironment,
        expected: String,
    ) = runTest {
        givenBackendEnvironment(null)
        givenBuildInfo(fixture())
        coJustRun {
            backendEnvironmentDataSource.set(expected)
        }
        val repo = initRepo(UnconfinedTestDispatcher())

        repo.set(input)

        coVerify(exactly = 1) {
            backendEnvironmentDataSource.set(expected)
        }
    }

    private fun givenBuildInfo(result: BuildInfo) {
        every { buildInfoDataSource.data } returns result
    }

    private fun givenBackendEnvironment(result: String?) {
        every { backendEnvironmentDataSource.data } returns flowOf(result)
    }

    private fun initRepo(
        dispatcher: TestDispatcher,
        buildInfoDataSource: BuildInfoDataSource = this.buildInfoDataSource,
        backendEnvironmentDataSource: BackendEnvironmentDataSource = this.backendEnvironmentDataSource,
    ): ProjectSettingsRepositoryImpl {
        return ProjectSettingsRepositoryImpl(
            io = IoCoroutineDispatcher(dispatcher),
            buildInfoDataSource = buildInfoDataSource,
            backendEnvironmentDataSource = backendEnvironmentDataSource,
        )
    }

    private object InvalidBackendEnvironmentArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    true,
                    "any",
                    BackendEnvironment.PRODUCTION,
                ),
                Arguments.of(
                    false,
                    "any",
                    BackendEnvironment.STAGING,
                ),
                Arguments.of(
                    true,
                    null,
                    BackendEnvironment.PRODUCTION,
                ),
                Arguments.of(
                    false,
                    null,
                    BackendEnvironment.STAGING,
                ),
            )
        }
    }

    private object SetBackendEnvironmentArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    BackendEnvironment.PRODUCTION,
                    "production",
                ),
                Arguments.of(
                    BackendEnvironment.STAGING,
                    "staging",
                ),
                Arguments.of(
                    BackendEnvironment.QA,
                    "qa",
                ),
            )
        }
    }
}
