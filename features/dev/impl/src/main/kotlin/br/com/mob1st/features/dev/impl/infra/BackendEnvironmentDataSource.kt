package br.com.mob1st.features.dev.impl.infra

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import br.com.mob1st.core.androidx.datastore.DataStoreFile
import br.com.mob1st.core.androidx.datastore.PreferencesDataSource
import br.com.mob1st.core.androidx.datastore.set

internal class BackendEnvironmentDataSource(
    context: Context,
) : PreferencesDataSource<String?>(context, DataStoreFile.BE_ENV) {
    override suspend fun get(preferences: Preferences): String? =
        preferences[stringPreferencesKey(KEY_BACKEND_ENVIRONMENT)]

    override suspend fun set(
        preferences: MutablePreferences,
        data: String?,
    ) {
        preferences[stringPreferencesKey(KEY_BACKEND_ENVIRONMENT)] = data
    }

    companion object {
        private const val KEY_BACKEND_ENVIRONMENT = "backend_environment"
    }
}
