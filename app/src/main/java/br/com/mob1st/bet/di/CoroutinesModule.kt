package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutinesModule = module {
    single<DispatcherProvider> { DispatcherProvider }
}