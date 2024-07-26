package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.fixtures.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.fixtures.DayOfYear
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrenceType
import kotlinx.parcelize.Parceler

/**
 * Parcels a [Recurrences] object.
 */
internal object RecurrencesParceler : Parceler<Recurrences> {
    private enum class Type {
        Fixed,
        Seasonal,
        Variable,
    }

    override fun create(parcel: Parcel): Recurrences {
        val type = Type.entries[parcel.readInt()]
        return when (type) {
            Type.Fixed -> {
                val dayOfMonth = DayOfMonth(parcel.readInt())
                Recurrences.Fixed(dayOfMonth)
            }

            Type.Seasonal -> {
                val daysOfYear = parcel.createIntArray()?.map {
                    DayOfYear(it)
                }.orEmpty()
                Recurrences.Seasonal(daysOfYear)
            }

            Type.Variable -> Recurrences.Variable
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
