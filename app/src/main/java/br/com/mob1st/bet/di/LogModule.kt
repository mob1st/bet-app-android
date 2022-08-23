package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.logs.Logger
import br.com.mob1st.bet.core.timber.TimberLogger
import org.koin.dsl.module

val logModule = module {
    factory<Logger> { param ->
        TimberLogger(param.get())
    }
}