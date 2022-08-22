package br.com.mob1st.bet.di

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
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
}