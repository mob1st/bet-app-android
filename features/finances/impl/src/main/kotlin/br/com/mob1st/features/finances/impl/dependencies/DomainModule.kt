package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.usecases.GetBudgetBuilderForStepUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryDetailUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.SetCategoryUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.StartBuilderStepUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val domainModule = module {
    factoryOf(::GetBudgetBuilderForStepUseCase)
    factoryOf(::StartBuilderStepUseCase)
    factoryOf(::ProceedBuilderUseCase)
    factoryOf(::GetCategoryDetailUseCase)
    factoryOf(::SetCategoryUseCase)
}
