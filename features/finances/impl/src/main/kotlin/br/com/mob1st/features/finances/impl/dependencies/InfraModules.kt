package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.impl.domain.repositories.SuggestionsRepository
import br.com.mob1st.features.finances.impl.infra.data.preferences.RecurrenceBuilderCompletionsDataSource
import br.com.mob1st.features.finances.impl.infra.data.ram.RecurrenceBuilderListsDataSource
import br.com.mob1st.features.finances.impl.infra.data.repositories.RecurrenceBuilderRepositoryImpl
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.CategoriesRepositoryImpl
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.SelectCategoryViewsMapper
import br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions.SelectSuggestionsMapper
import br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions.SuggestionsRepositoryImpl
import br.com.mob1st.features.finances.impl.infra.data.sqldelight.DatabaseFactory
import br.com.mob1st.features.finances.impl.infra.data.system.AndroidAssetsGetter
import br.com.mob1st.features.finances.impl.infra.data.system.AndroidRecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.infra.data.system.AndroidStringIdGetter
import br.com.mob1st.features.finances.impl.infra.data.system.AssetsGetter
import br.com.mob1st.features.finances.impl.infra.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.infra.data.system.StringIdGetter
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val infraModule
    get() = module {
        includes(
            repositoriesModule,
            systemModule,
        )
    }

private val repositoriesModule = module {
    single {
        DatabaseFactory.create(get())
    }
    factory<RecurrenceBuilderRepository> {
        RecurrenceBuilderRepositoryImpl(
            listsDataSource = get<RecurrenceBuilderListsDataSource>(),
            completionsDataSource = get<RecurrenceBuilderCompletionsDataSource>(),
            io = get(),
        )
    }
    factory { SelectCategoryViewsMapper }
    factoryOf(::SelectSuggestionsMapper)
    factoryOf(::CategoriesRepositoryImpl) bind CategoriesRepository::class
    factoryOf(::SuggestionsRepositoryImpl) bind SuggestionsRepository::class
}

private val systemModule = module {
    factoryOf(::AndroidRecurrenceLocalizationProvider) bind RecurrenceLocalizationProvider::class
    factoryOf(::RecurrenceBuilderCompletionsDataSource)
    factoryOf(::AndroidStringIdGetter) bind StringIdGetter::class
    factory {
        AndroidAssetsGetter
    } bind AssetsGetter::class
}
