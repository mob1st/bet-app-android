package br.com.mob1st.features.dev.impl.menu.domain

import br.com.mob1st.features.dev.publicapi.domain.ProjectSettingsRepository
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

/**
 * Use case to get the menu data
 */
@Factory
internal class GetMenuUseCase(
    private val projectSettingsRepository: ProjectSettingsRepository,
) {
    operator fun invoke() = projectSettingsRepository
        .get()
        .map { it.backendEnvironment }
}
