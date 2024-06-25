package br.com.mob1st.features.finances.impl.domain.values

@JvmInline
value class DayOfWeek(val value: Int) {
    init {
        require(value in 0..LAST_DAY_IN_WEEK) {
            ""
        }
    }

    companion object {
        private const val LAST_DAY_IN_WEEK = 6
    }
}
