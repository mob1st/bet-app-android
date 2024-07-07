package br.com.mob1st.features.finances.impl.ui.utils.texts

import br.com.mob1st.core.design.atoms.properties.texts.TextState
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
    fun create(recurrences: Recurrences): TextState? {
        return when (recurrences) {
            is Recurrences.Fixed -> FixedRecurrencesTextState(recurrences)
            is Recurrences.Seasonal -> SeasonalRecurrencesTextState(recurrences)
            is Recurrences.Variable -> null
        }
    }
}