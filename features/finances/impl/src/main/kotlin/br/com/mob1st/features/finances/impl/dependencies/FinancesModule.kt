package br.com.mob1st.features.finances.impl.dependencies

import org.koin.dsl.module

val financesModule
    get() = module {
        includes(
            domainModule,
            infraModule,
            uiModule,
        )
    }
