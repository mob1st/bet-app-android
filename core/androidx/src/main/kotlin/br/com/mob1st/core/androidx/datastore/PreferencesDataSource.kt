package br.com.mob1st.core.androidx.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import br.com.mob1st.core.data.UnitaryDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Get and set data [DataStore] of type [Preferences].
 * It requires only the mapping between [Preferences] and the type of data to be persisted.
 *
 * @param T the type of data to be persisted.
 */
abstract class PreferencesDataSource<T>(
    private val context: Context,
    dataStoreFile: DataStoreFile,
) : UnitaryDataSource<T> {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(dataStoreFile.name)

    /**
     * Get data from [DataStore].
     */
    override val data: Flow<T> = context.dataStore.data.map(::get)

    /**
     * Set data in [DataStore].
     */
    override suspend fun set(data: T) {
        context.dataStore.edit { set(it, data) }
    }

    /**
     * Get data from [Preferences].
     */
    protected abstract suspend fun get(preferences: Preferences): T

    /**
     * Set data in [MutablePreferences].
     */
    protected abstract suspend fun set(preferences: MutablePreferences, data: T)
}
