package br.com.mob1st.features.finances.impl.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import br.com.mob1st.core.androidx.datastore.DataStoreFile
import br.com.mob1st.core.androidx.datastore.PreferencesDataSource

/**
 * Data source for [RecurrenceBuilderCompletions].
 * It uses DataStore API to store the data in a [Preferences] style structure.
 */
internal class RecurrenceBuilderCompletionsDataSource(
    context: Context,
) : PreferencesDataSource<RecurrenceBuilderCompletions>(context, DataStoreFile.RECURRENCE_BUILDER_STEP_COMPLETION) {
    override suspend fun get(preferences: Preferences): RecurrenceBuilderCompletions {
        return RecurrenceBuilderCompletions(
            isFixedExpansesCompleted = preferences[booleanPreferencesKey(FIXED_STEP_COMPLETION_KEY)] ?: false,
            isVariableExpansesCompleted = preferences[booleanPreferencesKey(VARIABLE_STEP_COMPLETION_KEY)] ?: false,
            isSeasonalExpansesCompleted = preferences[booleanPreferencesKey(SEASONAL_STEP_COMPLETION_KEY)] ?: false,
            isIncomesCompleted = preferences[booleanPreferencesKey(INCOMES_STEP_COMPLETION_KEY)] ?: false,
        )
    }

    override suspend fun set(
        preferences: MutablePreferences,
        data: RecurrenceBuilderCompletions,
    ) {
        return with(preferences) {
            this[booleanPreferencesKey(FIXED_STEP_COMPLETION_KEY)] = data.isFixedExpansesCompleted
            this[booleanPreferencesKey(VARIABLE_STEP_COMPLETION_KEY)] = data.isVariableExpansesCompleted
            this[booleanPreferencesKey(SEASONAL_STEP_COMPLETION_KEY)] = data.isSeasonalExpansesCompleted
            this[booleanPreferencesKey(INCOMES_STEP_COMPLETION_KEY)] = data.isIncomesCompleted
        }
    }

    companion object {
        private const val FIXED_STEP_COMPLETION_KEY = "fixed_step_completion"
        private const val VARIABLE_STEP_COMPLETION_KEY = "variable_step_completion"
        private const val SEASONAL_STEP_COMPLETION_KEY = "seasonal_step_completion"
        private const val INCOMES_STEP_COMPLETION_KEY = "incomes_step_completion"
    }
}
