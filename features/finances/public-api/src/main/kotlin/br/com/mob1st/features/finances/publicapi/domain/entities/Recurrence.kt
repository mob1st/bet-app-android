package br.com.mob1st.features.finances.publicapi.domain.entities

import kotlinx.datetime.Month

sealed interface Recurrence {

    data class Fixed(val dayOfMonth: Int = DEFAULT_DAY_OF_MONTH) : Recurrence {
        companion object {
            const val DEFAULT_DAY_OF_MONTH: Int = 5
        }
    }
    data object Variable : Recurrence
    data class Seasonal(val month: Month, val day: Int) : Recurrence
}
