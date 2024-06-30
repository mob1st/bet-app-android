package br.com.mob1st.features.finances.impl.utils

import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfWeek
import br.com.mob1st.features.finances.impl.domain.values.Month
import br.com.mob1st.tests.featuresutils.defaultFixtures

/**
 * Create fixtures for values specific in this module
 */
val moduleFixture = defaultFixtures.new {
    factory<DayOfMonth> {
        DayOfMonth((1..31).random())
    }
    factory<DayOfWeek> {
        DayOfWeek((1..7).random())
    }
    factory<Month> {
        Month((1..12).random())
    }
}
