package br.com.mob1st.libs.firebase.remoteconfig

import com.google.firebase.remoteconfig.ktx.BuildConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

private const val DEBUG_INTERVAL_MS = 3_600L
private const val PROD_INTERVAL_SEC = 5L

/**
 * Remote config settings.
 */
fun remoteConfigSettings() = remoteConfigSettings {
    if (BuildConfig.DEBUG) {
        minimumFetchIntervalInSeconds = DEBUG_INTERVAL_MS
    } else {
        fetchTimeoutInSeconds = PROD_INTERVAL_SEC
    }
}
