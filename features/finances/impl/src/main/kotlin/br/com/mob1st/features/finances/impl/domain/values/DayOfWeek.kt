package br.com.mob1st.features.finances.impl.domain.values

@JvmInline
value class DayOfWeek(val value: Int) {
    init {
        require(value in 1..LAST_DAY_IN_WEEK) {
            "week days value $value is not between 1 and $LAST_DAY_IN_WEEK"
        }
    }

    companion object {
        private const val LAST_DAY_IN_WEEK = 7
    }
}
