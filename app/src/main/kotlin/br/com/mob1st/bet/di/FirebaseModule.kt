package br.com.mob1st.bet.di

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.firebase.CrashlyticsTool
import br.com.mob1st.bet.core.firebase.GoogleAnalyticsTool
import br.com.mob1st.bet.core.logs.CrashReportingTool
import br.com.mob1st.core.observability.crashes.CrashReporter
import br.com.mob1st.core.observability.events.AnalyticsReporter
import br.com.mob1st.libs.firebase.analytics.FirebaseAnalyticsReporter
import br.com.mob1st.libs.firebase.crashlytics.FirebaseCrashReporter
import br.com.mob1st.libs.firebase.firestore.firestoreSettings
import br.com.mob1st.libs.firebase.remoteconfig.remoteConfigSettings
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.remoteconfig.ktx.remoteConfig
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
        Firebase.firestore.also {
            it.firestoreSettings = firestoreSettings()
        }
    }
    single {
        Firebase.remoteConfig.also {
            it.setConfigSettingsAsync(remoteConfigSettings())
        }
    }
    factoryOf(::FirebaseAnalyticsReporter).bind<AnalyticsReporter>()
    factoryOf(::FirebaseCrashReporter).bind<CrashReporter>()

    factoryOf(::GoogleAnalyticsTool).bind<AnalyticsTool>()
    factoryOf(::CrashlyticsTool).bind<CrashReportingTool>()
}
