package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.events.BuilderStepScreenViewFactory
import br.com.mob1st.features.finances.impl.domain.usecases.GetCashFlowUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryBuilderUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val domainModule
    get() = module {
        single {
            BuilderStepScreenViewFactory
        }
        factoryOf(::GetCategoryBuilderUseCase)
        factoryOf(::GetCashFlowUseCase)
    }
