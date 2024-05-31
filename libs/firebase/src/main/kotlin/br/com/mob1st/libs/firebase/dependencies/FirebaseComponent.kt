package br.com.mob1st.libs.firebase.dependencies

import br.com.mob1st.libs.firebase.firestore.firestoreSettings
import br.com.mob1st.libs.firebase.remoteconfig.remoteConfigSettings
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.remoteconfig.ktx.remoteConfig
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("br.com.mob1st.libs.firebase")
class FirebaseComponent {
    @Single
    fun app() = Firebase.app

    @Single
    fun auth() = Firebase.auth

    @Single
    fun crashlytics() = Firebase.crashlytics

    @Single
    fun analytics() = Firebase.analytics

    @Single
    fun firestore() =
        Firebase.firestore.also {
            it.firestoreSettings = firestoreSettings()
        }

    @Single
    fun remoteConfig() =
        Firebase.remoteConfig.also {
            it.setConfigSettingsAsync(remoteConfigSettings())
        }
}
