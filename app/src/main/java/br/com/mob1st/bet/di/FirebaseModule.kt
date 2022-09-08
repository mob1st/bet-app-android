package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.firebase.CrashlyticsTool
import br.com.mob1st.bet.core.firebase.GoogleAnalyticsTool
import br.com.mob1st.bet.core.logs.CrashReportingTool
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firebaseModule = module {
    single {
        Firebase.app
    }
    single {
        Firebase.auth
    }
    single {
        Firebase.crashlytics
    }
    single {
        Firebase.analytics
    }
    single {
        Firebase.firestore
    }
    factoryOf(::GoogleAnalyticsTool).bind<AnalyticsTool>()
    factoryOf(::CrashlyticsTool).bind<CrashReportingTool>()
}