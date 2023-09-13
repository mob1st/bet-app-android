package br.com.mob1st.features.dev.impl.menu.data

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettings
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
internal class ProjectSettingsRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val buildInfoDataSource: BuildInfoDataSource,
    private val backendEnvironmentDataSource: BackendEnvironmentDataSource,
) : ProjectSettingsRepository {

    private val beEnvIsomorphism = BackendEnvironmentIsomorphism(
        buildInfoDataSource.data.isReleaseBuild
    )

    override fun get(): Flow<ProjectSettings> = backendEnvironmentDataSource
        .get()
        .map { backendEnvironment ->
            val buildInfo = buildInfoDataSource.data
            ProjectSettings(
                buildInfo = buildInfo,
                backendEnvironment = beEnvIsomorphism().second[backendEnvironment]
            )
        }
        .flowOn(io)

    override suspend fun set(backendEnvironment: BackendEnvironment) = withContext(io) {
        backendEnvironmentDataSource.set(beEnvIsomorphism().first[backendEnvironment])
    }
}
