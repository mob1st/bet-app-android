package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.events.BuilderStepScreenViewFactory
import br.com.mob1st.features.finances.impl.domain.usecases.GetCashFlowUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.GetCategoryBuilderUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.GetFixedExpensesUseCaseImpl
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepViewModel
import br.com.mob1st.features.finances.impl.ui.navgraph.CategoryBuilderNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.navgraph.FinancesNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.tabs.CashFlowUiStateHolder
import br.com.mob1st.features.finances.impl.ui.tabs.CashFlowViewModel
import br.com.mob1st.features.finances.publicapi.domain.ui.CategoryBuilderNavGraph
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
            infraModule,
            uiModule,
        )
    }

private val domainModule = module {
    factory {
        BuilderStepScreenViewFactory
    }
    factoryOf(::GetFixedExpensesUseCaseImpl) bind GetFixedExpensesUseCase::class
    factoryOf(::GetCategoryBuilderUseCase)
    factoryOf(::GetCashFlowUseCase)
}

private val uiModule = module {
    single { FinancesNavGraphImpl } bind FinancesNavGraph::class
    single { CategoryBuilderNavGraphImpl } bind CategoryBuilderNavGraph::class
    factoryOf(::CashFlowUiStateHolder)
    viewModelOf(::BudgetBuilderStepViewModel)
    viewModelOf(::CashFlowViewModel)
}
