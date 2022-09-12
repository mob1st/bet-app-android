package br.com.mob1st.bet.core.firebase

import com.google.firebase.remoteconfig.ktx.BuildConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

val remoteConfigSettings = remoteConfigSettings {
    if (BuildConfig.DEBUG) {
        minimumFetchIntervalInSeconds = 3_600
    } else {
        fetchTimeoutInSeconds = 5
    }
}