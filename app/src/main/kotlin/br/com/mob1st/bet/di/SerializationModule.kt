package br.com.mob1st.bet.di

import br.com.mob1st.core.kotlinx.serialization.defaultJson
import org.koin.dsl.module

val serializationModule = module {
    single { defaultJson() }
}
