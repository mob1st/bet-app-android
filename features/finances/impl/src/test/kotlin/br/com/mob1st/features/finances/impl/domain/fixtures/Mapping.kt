package br.com.mob1st.features.finances.impl.domain.fixtures

import br.com.mob1st.core.kotlinx.structures.toBiMap
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth

val typeRecurrenceToRecurrences = RecurrenceType.entries.map { type ->
    type to when (type) {
        RecurrenceType.Fixed -> Recurrences.Fixed(DayOfMonth(1))
        RecurrenceType.Variable -> Recurrences.Variable
        RecurrenceType.Seasonal -> Recurrences.Seasonal(emptyList())
    }
}.toBiMap()
