package br.com.mob1st.features.finances.impl.domain.values

data class DayAndMonth(
    val day: DayOfMonth,
    val month: Month,
)

@JvmInline
value class Month(val value: Int) {
    init {
        require(value in 1..MONTHS_IN_A_YEAR) {
            ""
        }
    }

    companion object {
        private const val MONTHS_IN_A_YEAR = 12
        val January = Month(1)
        val February = Month(2)
        val March = Month(3)
        val April = Month(4)
        val May = Month(5)
        val June = Month(6)
        val July = Month(7)
        val August = Month(8)
        val September = Month(9)
        val October = Month(10)
        val November = Month(11)
        val December = Month(12)
    }
}
