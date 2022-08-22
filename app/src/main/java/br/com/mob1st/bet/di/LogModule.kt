package br.com.mob1st.bet.di

import org.koin.dsl.module
import timber.log.Timber

val logModule = module {
    factory {
        Timber.asTree()
    }
}