package br.com.mob1st.features.finances.impl.domain.values

@JvmInline
value class DayOfMonth(val value: Int) {
    init {
        require(value in 1..DAYS_IN_A_MONTH) {
            "value $value should be in between 1 and 31"
        }
    }

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        private const val DAYS_IN_A_MONTH = 31
        val allDays: List<DayOfMonth> = (1..DAYS_IN_A_MONTH).map { DayOfMonth(it) }
    }
}
