package br.com.mob1st.bet.core.firebase

import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.remoteconfig.ktx.BuildConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

private const val DEBUG_INTERVAL_MS = 3_600L
private const val PROD_INTERVAL_SEC = 5L

val remoteConfigSettings = remoteConfigSettings {
    if (BuildConfig.DEBUG) {
        minimumFetchIntervalInSeconds = DEBUG_INTERVAL_MS
    } else {
        fetchTimeoutInSeconds = PROD_INTERVAL_SEC
    }
}

val firestoreSettings = firestoreSettings {
    isPersistenceEnabled = false
}
