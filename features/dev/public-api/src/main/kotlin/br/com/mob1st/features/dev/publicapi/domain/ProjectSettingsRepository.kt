package br.com.mob1st.features.dev.publicapi.domain

import kotlinx.coroutines.flow.Flow

/**
 * Provides the project settings data.
 */
interface ProjectSettingsRepository {
    /**
     * The project settings data.
     */
    fun get(): Flow<ProjectSettings>

    /**
     * Sets the backend environment.
     */
    suspend fun set(backendEnvironment: BackendEnvironment)
}
