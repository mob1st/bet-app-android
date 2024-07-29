package br.com.mob1st.bet.di

import br.com.mob1st.core.kotlinx.coroutines.AppScopeProvider
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.core.kotlinx.coroutines.MainCoroutineDispatcher
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val coroutinesModule = module {
    single { androidApplication() as AppScopeProvider }
    factory { IoCoroutineDispatcher() }
    factory { DefaultCoroutineDispatcher() }
    factory { MainCoroutineDispatcher() }
}
