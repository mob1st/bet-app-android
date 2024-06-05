package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.data.preferences.RecurrenceBuilderCompletionsDataSource
import br.com.mob1st.features.finances.impl.data.ram.RecurrenceBuilderListsDataSource
import br.com.mob1st.features.finances.impl.data.repositories.RecurrenceBuilderRepositoryImpl
import br.com.mob1st.features.finances.impl.data.system.AndroidRecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.impl.domain.usecases.GetFixedExpensesUseCaseImpl
import br.com.mob1st.features.finances.impl.domain.usecases.GetOperationListUseCase
import br.com.mob1st.features.finances.impl.ui.FinancesNavGraphImpl
import br.com.mob1st.features.finances.impl.ui.OperationListUiStateHolder
import br.com.mob1st.features.finances.impl.ui.OperationListViewModel
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
}

private val dataModule = module {
    factory<RecurrenceBuilderRepository> {
        RecurrenceBuilderRepositoryImpl(
            listsDataSource = get<RecurrenceBuilderListsDataSource>(),
            completionsDataSource = get<RecurrenceBuilderCompletionsDataSource>(),
            io = get(),
        )
    }
    factoryOf(::AndroidRecurrenceLocalizationProvider) bind RecurrenceLocalizationProvider::class
    factoryOf(::RecurrenceBuilderCompletionsDataSource)
    factoryOf(::GetOperationListUseCase)
}

private val uiModule = module {
    single { FinancesNavGraphImpl } bind FinancesNavGraph::class
    factoryOf(::OperationListUiStateHolder)
    viewModelOf(::OperationListViewModel)
}
