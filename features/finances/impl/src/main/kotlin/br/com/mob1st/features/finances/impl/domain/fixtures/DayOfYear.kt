package br.com.mob1st.features.finances.impl.domain.fixtures

@JvmInline
value class DayOfYear(val value: Int) {
    init {
        require(value in 1..DAYS_IN_A_YEAR) {
            "The day of year must be between 1 and $DAYS_IN_A_YEAR. Current value: $value."
        }
    }

    companion object {
        private const val DAYS_IN_A_YEAR = 365
    }
}
