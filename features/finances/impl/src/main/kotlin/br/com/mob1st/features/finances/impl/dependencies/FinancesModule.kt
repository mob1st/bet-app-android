package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.usecases.GetCashFlowUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryBuilderUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.GetFixedExpensesUseCaseImpl
import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderStepUseCase
import br.com.mob1st.features.finances.impl.ui.navgraph.FinancesNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.tabs.CashFlowUiStateHolder
import br.com.mob1st.features.finances.impl.ui.tabs.CashFlowViewModel
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import br.com.mob1st.features.finances.publicapi.domain.usecases.GetFixedExpensesUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val financesModule
    get() = module {
        includes(
            domainModule,
            dataModule,
            uiModule,
        )
    }

private val domainModule = module {
    factoryOf(::GetFixedExpensesUseCaseImpl) bind GetFixedExpensesUseCase::class
    factoryOf(::GetCategoryBuilderUseCase)
    factoryOf(::GetCashFlowUseCase)
    factoryOf(::ProceedBuilderStepUseCase)
}

private val uiModule = module {
    single { FinancesNavGraphImpl } bind FinancesNavGraph::class
    factoryOf(::CashFlowUiStateHolder)
    viewModelOf(::CashFlowViewModel)
}
