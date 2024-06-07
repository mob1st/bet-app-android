package br.com.mob1st.features.twocents.builder.impl.dependencies

import br.com.mob1st.features.twocents.builder.impl.ui.navigation.BuilderNavGraphImpl
import br.com.mob1st.features.twocents.builder.publicapi.BuilderNavGraph
import org.koin.dsl.bind
import org.koin.dsl.module

val builderModule
    get() = module {
        includes(uiModule)
    }

private val uiModule = module {
    factory { BuilderNavGraphImpl } bind BuilderNavGraph::class
}
