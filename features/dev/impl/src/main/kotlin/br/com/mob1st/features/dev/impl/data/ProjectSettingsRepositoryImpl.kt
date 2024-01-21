package br.com.mob1st.features.dev.impl.data

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettings
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class ProjectSettingsRepositoryImpl(
    private val buildInfoDataSource: BuildInfoDataSource,
    private val backendEnvironmentDataSource: BackendEnvironmentDataSource,
    private val io: IoCoroutineDispatcher,
) : ProjectSettingsRepository {

    private val beEnvIsomorphism = backendEnvironmentIsomorphism(
        buildInfoDataSource.data.isReleaseBuild
    )

    override fun get(): Flow<ProjectSettings> = backendEnvironmentDataSource
        .data
        .map { backendEnvironment ->
            val buildInfo = buildInfoDataSource.data
            ProjectSettings(
                buildInfo = buildInfo,
                backendEnvironment = beEnvIsomorphism.second[backendEnvironment]
            )
        }

    override suspend fun set(backendEnvironment: BackendEnvironment) = withContext(io) {
        backendEnvironmentDataSource.set(beEnvIsomorphism.first[backendEnvironment])
    }
}
