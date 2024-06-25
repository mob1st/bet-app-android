package br.com.mob1st.features.finances.impl.domain.values

@JvmInline
value class DayOfMonth(val value: Int) {
    init {
        require(value in 1..DAYS_IN_A_MONTH) {
            "value $value should be in between 1 and 31"
        }
    }

    companion object {
        private const val DAYS_IN_A_MONTH = 31
    }
}
