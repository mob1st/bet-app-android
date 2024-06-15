package br.com.mob1st.features.twocents.builder.impl.dependencies

import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.GetSuggestionsUseCase
import br.com.mob1st.features.twocents.builder.impl.domain.usecases.SetCategoryBatchUseCase
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderStateRestorer
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderUiState
import br.com.mob1st.features.twocents.builder.impl.ui.builder.BuilderViewModel
import br.com.mob1st.features.twocents.builder.impl.ui.navigation.BuilderNavGraphImpl
import br.com.mob1st.features.twocents.builder.publicapi.BuilderNavGraph
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val builderModule
    get() = module {
        includes(domainModule, uiModule)
    }

private val domainModule = module {
    factoryOf(::GetSuggestionsUseCase)
    factoryOf(::SetCategoryBatchUseCase)
}

private val uiModule = module {
    factory { BuilderNavGraphImpl } bind BuilderNavGraph::class
    viewModel { (type: CategoryType) ->
        BuilderViewModel(
            initialState = BuilderUiState(type),
            builderStateRestorer = BuilderStateRestorer(savedStateHandle = get()),
            getSuggestionsUseCase = get(),
            setCategoryBatchUseCase = get(),
        )
    }
}
