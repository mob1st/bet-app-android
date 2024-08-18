package br.com.mob1st.features.finances.impl.domain.values

import kotlinx.datetime.Month
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.Locale

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

    @ParameterizedTest
    @MethodSource("monthToDaySource")
    fun `GIVEN a month WHEN create the day of year from it THEN assert return`(
        month: Month,
        expectedDayOfYear: Int,
    ) {
        val dayOfYear = DayOfYear.fromMonth(month)
        assertEquals(
            expectedDayOfYear,
            dayOfYear.value,
        )
    }

    @ParameterizedTest
    @MethodSource("dayOfYearToMonthIndexSource")
    fun `GIVEN a day of year WHEN get selected month THEN assert it returns the index of the month this day belongs to`(
        dayOfYear: Int,
        expectedMonth: Int,
    ) {
        val subject = DayOfYear(dayOfYear)
        val actual = subject.selectedMonth()
        assertEquals(
            expectedMonth,
            actual,
        )
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

        @JvmStatic
        fun monthToDaySource() = listOf(
            arguments(
                Month.JANUARY,
                1,
            ),
            arguments(
                Month.FEBRUARY,
                32,
            ),
            arguments(
                Month.MARCH,
                60,
            ),
            arguments(
                Month.APRIL,
                91,
            ),
            arguments(
                Month.MAY,
                121,
            ),
            arguments(
                Month.JUNE,
                152,
            ),
            arguments(
                Month.JULY,
                182,
            ),
            arguments(
                Month.AUGUST,
                213,
            ),
            arguments(
                Month.SEPTEMBER,
                244,
            ),
            arguments(
                Month.OCTOBER,
                274,
            ),
            arguments(
                Month.NOVEMBER,
                305,
            ),
            arguments(
                Month.DECEMBER,
                335,
            ),
        )

        @JvmStatic
        fun dayOfYearToMonthIndexSource() = listOf(
            arguments(
                1,
                0,
            ),
            arguments(
                32,
                1,
            ),
            arguments(
                60,
                2,
            ),
            arguments(
                335,
                11,
            ),
            arguments(
                365,
                11,
            ),
        )
    }
}
