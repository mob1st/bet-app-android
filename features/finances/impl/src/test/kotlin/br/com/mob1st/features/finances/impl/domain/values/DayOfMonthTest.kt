package br.com.mob1st.features.finances.impl.domain.values

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class DayOfMonthTest {
    @Test
    fun `WHEN get the list of all days THEN assert its size is 31`() {
        assertEquals(
            (1..31).toList(),
            DayOfMonth.allDays.map { it.value },
        )
    }
}
