package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroViewModel
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRouter
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepViewModel
import br.com.mob1st.features.finances.impl.ui.navgraph.BudgetBuilderNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.navgraph.FinancesNavGraphImpl
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

internal val uiModule
    get() = module {
        factory { FinancesNavGraphImpl } bind FinancesNavGraph::class
        includes(builderModule)
    }

internal val builderModule = module {
    factory { BuilderRouter }
    factory { BudgetBuilderNavGraphImpl(get()) } bind BudgetBuilderNavGraph::class
    viewModel {
        BuilderIntroViewModel(
            consumableDelegate = BuilderIntroViewModel.consumableDelegate(),
            startBuilderStep = get(),
            default = get(),
        )
    }
    viewModel { params ->
        BudgetBuilderStepViewModel(
            default = get(),
            consumableDelegate = BudgetBuilderStepViewModel.consumableDelegate(),
            step = params.get(),
            getCategoryBuilder = get(),
            proceedBuilder = get(),
        )
    }
}
