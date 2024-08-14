package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.ui.builder.intro.BuilderIntroViewModel
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BudgetBuilderNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderCoordinator
import br.com.mob1st.features.finances.impl.ui.builder.steps.BudgetBuilderStepViewModel
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryStateHandle
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryViewModel
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryCoordinator
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.navgraph.FinancesNavGraphImpl
import br.com.mob1st.features.finances.publicapi.domain.ui.BudgetBuilderNavGraph
import br.com.mob1st.features.finances.publicapi.domain.ui.CategoryNavGraph
import br.com.mob1st.features.finances.publicapi.domain.ui.FinancesNavGraph
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val uiModule
    get() = module {
        factoryOf(::FinancesNavGraphImpl) bind FinancesNavGraph::class
        includes(categoriesModule)
        includes(builderModule)
    }

private val categoriesModule = module {
    factoryOf(::CategoryCoordinator)
    factoryOf(::CategoryNavGraphImpl) bind CategoryNavGraph::class
    viewModel { params ->
        CategoryViewModel(
            default = get(),
            consumableDelegate = CategoryViewModel.consumableDelegate(),
            intent = params.get(),
            getCategoryDetail = get(),
            setCategory = get(),
            categoryStateHandle = CategoryStateHandle(get()),
        )
    }
}

internal val builderModule = module {
    factoryOf(::BuilderCoordinator)
    factoryOf(::BudgetBuilderNavGraphImpl) bind BudgetBuilderNavGraph::class
    viewModel {
        BuilderIntroViewModel(
            consumableDelegate = BuilderIntroViewModel.consumableDelegate(),
            startBuilderStep = get(),
            default = get(),
        )
    }
    viewModel { params ->
        BudgetBuilderStepViewModel(
            consumableDelegate = BudgetBuilderStepViewModel.consumableDelegate(),
            default = get(),
            step = params.get(),
            getCategoryBuilder = get(),
            proceedBuilder = get(),
        )
    }
}
