package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.CategorySuggestionRepository
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.impl.infra.data.preferences.RecurrenceBuilderCompletionsDataSource
import br.com.mob1st.features.finances.impl.infra.data.ram.RecurrenceBuilderListsDataSource
import br.com.mob1st.features.finances.impl.infra.data.repositories.RecurrenceBuilderRepositoryImpl
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.CategoryRepositoryImpl
import br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions.CategorySuggestionsRepositoryImpl
import br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions.SuggestionListPerStep
import br.com.mob1st.features.finances.impl.infra.data.sqldelight.DatabaseFactory
import br.com.mob1st.features.finances.impl.infra.data.system.AndroidRecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.infra.data.system.RecurrenceLocalizationProvider
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
    factory { SuggestionListPerStep }
    factory<RecurrenceBuilderRepository> {
        RecurrenceBuilderRepositoryImpl(
            listsDataSource = get<RecurrenceBuilderListsDataSource>(),
            completionsDataSource = get<RecurrenceBuilderCompletionsDataSource>(),
            io = get(),
        )
    }
    factoryOf(::CategoryRepositoryImpl) bind CategoriesRepository::class
    factoryOf(::CategorySuggestionsRepositoryImpl) bind CategorySuggestionRepository::class
}

private val systemModule = module {
    factoryOf(::AndroidRecurrenceLocalizationProvider) bind RecurrenceLocalizationProvider::class
    factoryOf(::RecurrenceBuilderCompletionsDataSource)
}
