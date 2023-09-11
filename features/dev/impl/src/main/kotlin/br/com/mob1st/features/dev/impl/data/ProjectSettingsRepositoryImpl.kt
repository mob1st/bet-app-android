package br.com.mob1st.features.dev.impl.data

import br.com.mob1st.core.kotlinx.coroutines.IO
import br.com.mob1st.features.dev.publicapi.domain.BackendEnvironment
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettings
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class ProjectSettingsRepositoryImpl(
    @Named(IO)
    private val io: CoroutineDispatcher = Dispatchers.IO,
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
                backendEnvironment = beEnvIsomorphism[backendEnvironment]
            )
        }
        .flowOn(io)

    override suspend fun set(backendEnvironment: BackendEnvironment) = withContext(io) {
        backendEnvironmentDataSource.set(beEnvIsomorphism[backendEnvironment])
    }
}
