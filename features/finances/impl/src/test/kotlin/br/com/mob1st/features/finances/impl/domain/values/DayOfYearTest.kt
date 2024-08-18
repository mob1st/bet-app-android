package br.com.mob1st.features.finances.impl.domain.values

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertFalse

class DayOfYearTest {
    @ParameterizedTest
    @MethodSource("dayOfYearAndFormat")
    fun `GIVEN a day of year WHEN format date THEN assert return`(
        dayOfYear: DayOfYear,
        expectedFormat: String,
    ) {
        val actual = dayOfYear.formatOfPattern(
            DATE_FORMAT,
            Locale.US,
        )
        assertEquals(
            expectedFormat,
            actual,
        )
    }

    @Test
    fun test() {
        assertFalse(true)
    }

    companion object {
        private const val DATE_FORMAT = "dd MMM"

        @JvmStatic
        fun dayOfYearAndFormat() = listOf(
            arguments(
                1,
                "01 Jan",
            ),
            arguments(
                32,
                "01 Feb",
            ),
            arguments(
                60,
                "01 Mar",
            ),
            arguments(
                91,
                "01 Apr",
            ),
            arguments(
                121,
                "01 May",
            ),
            arguments(
                152,
                "01 Jun",
            ),
            arguments(
                182,
                "01 Jul",
            ),
            arguments(
                213,
                "01 Aug",
            ),
            arguments(
                244,
                "01 Sep",
            ),
            arguments(
                274,
                "01 Oct",
            ),
            arguments(
                305,
                "01 Nov",
            ),
            arguments(
                335,
                "01 Dec",
            ),
            arguments(
                365,
                "31 Dec",
            ),
        )
    }
}
