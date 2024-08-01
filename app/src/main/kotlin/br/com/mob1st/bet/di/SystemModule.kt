package br.com.mob1st.bet.di

import br.com.mob1st.core.androidx.assets.AndroidAssetsGetter
import br.com.mob1st.core.androidx.assets.AssetsGetter
import br.com.mob1st.core.androidx.resources.AndroidStringGetter
import br.com.mob1st.core.androidx.resources.AndroidStringIdGetter
import br.com.mob1st.core.androidx.resources.StringGetter
import br.com.mob1st.core.androidx.resources.StringIdGetter
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides dependencies that has system dependencies such as the Android context.
 */
val systemModule = module {
    factory { AndroidStringGetter(androidContext()) } bind StringGetter::class
    factory { AndroidStringIdGetter(androidContext()) } bind StringIdGetter::class
    factory {
        AndroidAssetsGetter(
            context = androidContext(),
            io = get<IoCoroutineDispatcher>(),
        )
    } bind AssetsGetter::class
}
