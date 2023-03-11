package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.serialization.json
import org.koin.dsl.module

val serializationModule = module {
    single { json }
}
