package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.infra.repositories.AssetRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategoryRepository
import br.com.mob1st.features.finances.impl.domain.infra.repositories.CategorySuggestionRepository
import br.com.mob1st.features.finances.impl.infra.data.repositories.assets.AssetRepositoryImpl
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
    factoryOf(::CategoryRepositoryImpl) bind CategoryRepository::class
    factoryOf(::CategorySuggestionsRepositoryImpl) bind CategorySuggestionRepository::class
    factoryOf(::AssetRepositoryImpl) bind AssetRepository::class
}
