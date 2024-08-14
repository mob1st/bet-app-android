package br.com.mob1st.bet.di

import br.com.mob1st.twocents.core.navigation.NativeNavigationApi
import br.com.mob1st.twocents.core.navigation.android.StandardAndroidNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule = module {
    factoryOf(::StandardAndroidNavigationApi) bind NativeNavigationApi::class
}
