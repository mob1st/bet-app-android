package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.firebase.CrashlyticsTool
import br.com.mob1st.bet.core.firebase.GoogleAnalyticsTool
import br.com.mob1st.bet.core.logs.CrashReportingTool
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firebaseModule = module {
    factoryOf(::GoogleAnalyticsTool).bind<AnalyticsTool>()
    factoryOf(::CrashlyticsTool).bind<CrashReportingTool>()
}
