package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.coroutines.AppScopeProvider
import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val coroutinesModule = module {
    single<DispatcherProvider> { DispatcherProvider }
    single { androidApplication() as AppScopeProvider }
}