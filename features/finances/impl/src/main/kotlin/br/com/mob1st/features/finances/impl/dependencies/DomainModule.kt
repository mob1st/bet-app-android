package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.events.BuilderStepScreenViewFactory
import br.com.mob1st.features.finances.impl.domain.usecases.GetCashFlowUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryBuilderUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.GetFixedExpensesUseCaseImpl
import br.com.mob1st.features.finances.publicapi.domain.usecases.GetFixedExpensesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val domainModule
    get() = module {
        single {
            BuilderStepScreenViewFactory
        }
        factoryOf(::GetFixedExpensesUseCaseImpl) bind GetFixedExpensesUseCase::class
        factoryOf(::GetCategoryBuilderUseCase)
        factoryOf(::GetCashFlowUseCase)
    }
