package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.CategorySuggestionRepository
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.CategoriesDataMap
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.CategoryRepositoryImpl
import br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions.CategorySuggestionsRepositoryImpl
import br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions.SuggestionListPerStep
import br.com.mob1st.features.finances.impl.infra.data.sqldelight.DatabaseFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val infraModule
    get() = module {
        includes(
            repositoriesModule,
        )
    }

private val repositoriesModule = module {
    single {
        DatabaseFactory.create(get())
    }
    factory { SuggestionListPerStep() }
    single { CategoriesDataMap } bind CategoriesDataMap::class
    factoryOf(::CategoryRepositoryImpl) bind CategoriesRepository::class
    factoryOf(::CategorySuggestionsRepositoryImpl) bind CategorySuggestionRepository::class
}
