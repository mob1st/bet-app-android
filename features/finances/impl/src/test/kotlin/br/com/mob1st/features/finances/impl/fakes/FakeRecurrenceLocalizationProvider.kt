package br.com.mob1st.features.finances.impl.fakes

import br.com.mob1st.features.finances.impl.infra.data.system.RecurrenceLocalizationProvider

/**
 * Fake implementation of [FakeRecurrenceLocalizationProvider] that returns the suggestion name as the localized string
 */
internal fun FakeRecurrenceLocalizationProvider() =
    RecurrenceLocalizationProvider { suggestion ->
        suggestion.name
    }
