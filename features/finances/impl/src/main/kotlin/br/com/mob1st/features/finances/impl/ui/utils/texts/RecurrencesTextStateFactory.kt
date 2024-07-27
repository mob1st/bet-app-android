package br.com.mob1st.features.finances.impl.ui.utils.texts

import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences

/**
 * Provides the recurrences text state.
 */
object RecurrencesTextStateFactory {
    /**
     * Creates a text state from the given [recurrences].
     * @param recurrences The recurrences.
     * @return The text state or null if the recurrences are variable.
     */
    fun create(recurrences: Recurrences): LocalizedText? {
        return when (recurrences) {
            is Recurrences.Fixed -> FixedRecurrencesLocalizedText(recurrences)
            is Recurrences.Seasonal -> if (recurrences.daysOfYear.isNotEmpty()) {
                SeasonalRecurrencesLocalizedText(recurrences)
            } else {
                null
            }

            is Recurrences.Variable -> null
        }
    }
}
