package br.com.mob1st.features.dev.impl.menu.domain

import br.com.mob1st.features.dev.publicapi.domain.ProjectSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

/**
 * Use case to get the menu data
 */
@Factory
class GetDevMenuUseCase(
    private val projectSettingsRepository: ProjectSettingsRepository,
) {
    operator fun invoke(): Flow<DevMenu> = projectSettingsRepository
        .get()
        .map { DevMenu(it.backendEnvironment) }
}
