package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.core.timber.TimberLogger
import br.com.mob1st.bet.core.timber.TimberTreeFactory
import org.koin.dsl.module

val timberModule = module {
    single { TimberTreeFactory(get()).invoke() }
    factory<Logger> { param ->
        TimberLogger(param.get())
    }
}