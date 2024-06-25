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
    }
}
