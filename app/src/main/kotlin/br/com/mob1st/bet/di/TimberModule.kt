package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.core.timber.TimberLogger
import br.com.mob1st.bet.core.timber.TimberTreeFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val timberModule = module {
    single { TimberTreeFactory.create(get()) }
    factoryOf(::TimberLogger).bind<Logger>()
}
