package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import kotlinx.parcelize.Parceler

/**
 * Parcels a [Recurrences] object.
 */
internal object RecurrencesParceler : Parceler<Recurrences> {
    override fun create(parcel: Parcel): Recurrences {
        val type = RecurrenceType.entries[parcel.readInt()]
        return when (type) {
            RecurrenceType.Fixed -> {
                val dayOfMonth = DayOfMonth(parcel.readInt())
                Recurrences.Fixed(dayOfMonth)
            }

            RecurrenceType.Seasonal -> {
                val daysOfYear = parcel.createIntArray()?.map {
                    DayOfYear(it)
                }.orEmpty()
                Recurrences.Seasonal(daysOfYear)
            }

            RecurrenceType.Variable -> Recurrences.Variable
        }
    }

    override fun Recurrences.write(parcel: Parcel, flags: Int) {
        when (this) {
            is Recurrences.Fixed -> {
                parcel.writeInt(RecurrenceType.Fixed.ordinal)
                parcel.writeInt(day.value)
            }

            is Recurrences.Seasonal -> {
                parcel.writeInt(RecurrenceType.Seasonal.ordinal)
                parcel.writeIntArray(daysOfYear.map { it.value }.toIntArray())
            }

            is Recurrences.Variable -> {
                parcel.writeInt(RecurrenceType.Variable.ordinal)
            }
        }
    }
}
