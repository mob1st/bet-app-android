package br.com.mob1st.features.finances.impl.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import br.com.mob1st.core.androidx.datastore.DataStoreFile
import br.com.mob1st.core.androidx.datastore.PreferencesDataSource
import br.com.mob1st.core.data.UnitaryDataSource

internal class RecurrenceBuilderCompletionsDataSource(
    context: Context,
) : PreferencesDataSource<RecurrenceBuilderCompletions>(context, DataStoreFile.RECURRENCE_BUILDER_STEP),
    UnitaryDataSource<RecurrenceBuilderCompletions> {
    override suspend fun get(preferences: Preferences): RecurrenceBuilderCompletions {
        TODO()
    }

    override suspend fun set(preferences: MutablePreferences, data: RecurrenceBuilderCompletions) {
        TODO()
    }

    companion object {
        private const val KEY_CURRENT_BUILDER_STEP = "current_builder_step"
    }
}
