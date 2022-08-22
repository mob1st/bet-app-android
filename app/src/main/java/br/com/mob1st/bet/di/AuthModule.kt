package br.com.mob1st.bet.di

import br.com.mob1st.bet.features.auth.AuthRepository
import br.com.mob1st.bet.features.auth.OpenAppUseCase
import br.com.mob1st.bet.features.auth.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authModule = module {
    factoryOf(::OpenAppUseCase)
    factoryOf(::AuthRepository)
    viewModelOf(::SplashViewModel)
}