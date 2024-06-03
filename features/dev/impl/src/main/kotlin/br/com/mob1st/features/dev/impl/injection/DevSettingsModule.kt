package br.com.mob1st.features.dev.impl.injection

import br.com.mob1st.features.dev.impl.domain.GetDevMenuUseCase
import br.com.mob1st.features.dev.impl.infra.BackendEnvironmentDataSource
import br.com.mob1st.features.dev.impl.infra.ProjectSettingsRepositoryImpl
import br.com.mob1st.features.dev.impl.presentation.menu.DevMenuUiStateHolder
import br.com.mob1st.features.dev.impl.presentation.menu.DevMenuViewModel
import br.com.mob1st.features.dev.publicapi.domain.ProjectSettingsRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Module that provides dependencies for the dev settings feature.
 */
val devSettingsModule
    get() = module {
        includes(
            dataModule,
            domainModule,
            presentationModule,
        )
    }

private val dataModule = module {
    singleOf(::BackendEnvironmentDataSource)
}

private val domainModule = module {
    factoryOf(::GetDevMenuUseCase)
    factoryOf(::ProjectSettingsRepositoryImpl) bind ProjectSettingsRepository::class
}

private val presentationModule = module {
    factoryOf(::DevMenuUiStateHolder)
    viewModelOf(::DevMenuViewModel)
}
