package br.com.mob1st.bet.di

import android.app.Application
import br.com.mob1st.bet.features.dev.AppBuildInfoDataSource
import br.com.mob1st.core.kotlinx.coroutines.AppScopeProvider
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.core.kotlinx.coroutines.MainCoroutineDispatcher
import br.com.mob1st.core.kotlinx.serialization.defaultJson
import br.com.mob1st.features.dev.impl.infra.BuildInfoDataSource
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("br.com.mob1st.bet")
class AppModule {
    @Single
    fun json() = defaultJson()

    @Single
    fun appScope(application: Application) = (application as AppScopeProvider).appScope

    @Single
    fun buildInfoDataSource(): BuildInfoDataSource = AppBuildInfoDataSource

    @Single
    fun ioDispatcher() = IoCoroutineDispatcher()

    @Single
    fun mainDispatcher() = MainCoroutineDispatcher()

    @Single
    fun defaultDispatcher() = DefaultCoroutineDispatcher()
}
