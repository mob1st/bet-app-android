package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroViewModel
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepViewModel
import br.com.mob1st.features.finances.impl.ui.navgraph.BudgetBuilderNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.navgraph.FinancesNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.tabs.CashFlowUiStateHolder
import br.com.mob1st.features.finances.impl.ui.tabs.CashFlowViewModel
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val uiModule = module {
    factoryOf(::CashFlowUiStateHolder)
    viewModelOf(::CashFlowViewModel)
    single { FinancesNavGraphImpl } bind FinancesNavGraph::class
    includes(builderModule)
}

internal val builderModule = module {
    single { BudgetBuilderNavGraphImpl } bind BudgetBuilderNavGraph::class
    single { BuilderRouter }
    viewModelOf(::BuilderIntroViewModel)
    viewModelOf(::BudgetBuilderStepViewModel)
}
