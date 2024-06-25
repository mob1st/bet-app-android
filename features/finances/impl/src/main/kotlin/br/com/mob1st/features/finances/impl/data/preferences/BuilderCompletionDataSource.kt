package br.com.mob1st.features.finances.impl.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import br.com.mob1st.core.androidx.datastore.DataStoreFile
import br.com.mob1st.core.androidx.datastore.PreferencesDataSource

internal class BuilderCompletionDataSource(context: Context) : PreferencesDataSource<Boolean>(
    context,
    DataStoreFile.CATEGORY_BUILDER_COMPLETION,
) {
    override suspend fun set(preferences: MutablePreferences, data: Boolean) {
        preferences[booleanPreferencesKey(BUILDER_COMPLETED_KEY)] = data
    }

    override suspend fun get(preferences: Preferences): Boolean {
        return preferences[booleanPreferencesKey(BUILDER_COMPLETED_KEY)] ?: false
    }

    companion object {
        private const val BUILDER_COMPLETED_KEY = "BUILDER_COMPLETED_KEY"
    }
}
