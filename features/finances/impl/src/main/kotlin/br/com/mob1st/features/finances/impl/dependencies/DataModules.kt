package br.com.mob1st.features.finances.impl.dependencies

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import br.com.mob1st.core.database.FixedRecurrences
import br.com.mob1st.core.database.SeasonalRecurrences
import br.com.mob1st.core.database.VariableRecurrences
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.data.morphisms.CategoryDataMapper
import br.com.mob1st.features.finances.impl.data.morphisms.SuggestionDataMapper
import br.com.mob1st.features.finances.impl.data.preferences.RecurrenceBuilderCompletionsDataSource
import br.com.mob1st.features.finances.impl.data.ram.RecurrenceBuilderListsDataSource
import br.com.mob1st.features.finances.impl.data.repositories.RecurrenceBuilderRepositoryImpl
import br.com.mob1st.features.finances.impl.data.repositories.categories.CategoriesRepositoryImpl
import br.com.mob1st.features.finances.impl.data.repositories.suggestions.SuggestionsRepositoryImpl
import br.com.mob1st.features.finances.impl.data.system.AndroidRecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.impl.domain.repositories.SuggestionsRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule
    get() = module {
        includes(
            databaseModule,
            dataMappersModule,
            repositoriesModule,
        )
    }

private val databaseModule = module {
    single {
        TwoCentsDb(
            get(),
            FixedRecurrencesAdapter = FixedRecurrences.Adapter(
                day_of_monthAdapter = IntColumnAdapter,
            ),
            VariableRecurrencesAdapter = VariableRecurrences.Adapter(
                day_of_weekAdapter = IntColumnAdapter,
            ),
            SeasonalRecurrencesAdapter = SeasonalRecurrences.Adapter(
                dayAdapter = IntColumnAdapter,
                monthAdapter = IntColumnAdapter,
            ),
        )
    }
    factory {
        get<TwoCentsDb>().categoriesQueries
    }
    factory {
        get<TwoCentsDb>().suggestionsQueries
    }
}

private val dataMappersModule = module {
    factory { CategoryDataMapper }
    factoryOf(::SuggestionDataMapper)
}

private val repositoriesModule = module {
    factory<RecurrenceBuilderRepository> {
        RecurrenceBuilderRepositoryImpl(
            listsDataSource = get<RecurrenceBuilderListsDataSource>(),
            completionsDataSource = get<RecurrenceBuilderCompletionsDataSource>(),
            io = get(),
        )
    }

    factoryOf(::AndroidRecurrenceLocalizationProvider) bind RecurrenceLocalizationProvider::class
    factoryOf(::RecurrenceBuilderCompletionsDataSource)
    factoryOf(::CategoriesRepositoryImpl) bind CategoriesRepository::class
    factoryOf(::SuggestionsRepositoryImpl) bind SuggestionsRepository::class
}
