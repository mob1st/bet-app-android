package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.Month
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import kotlinx.parcelize.Parceler

/**
 * Parcels a [Recurrences] object.
 * It nests the [FixedRecurrencesParceler] and [SeasonalRecurrencesParceler] to parcel the inner objects.
 * In case of [Recurrences.Variable], no parceling is needed.

 */
internal object RecurrencesParceler : Parceler<Recurrences> {
    override fun create(parcel: Parcel): Recurrences {
        val type = CategoryType.entries[parcel.readInt()]
        return when (type) {
            CategoryType.Fixed -> FixedRecurrencesParceler.create(parcel)
            CategoryType.Seasonal -> SeasonalRecurrencesParceler.create(parcel)
            CategoryType.Variable -> Recurrences.Variable
        }
    }

    override fun Recurrences.write(parcel: Parcel, flags: Int) {
        when (this) {
            is Recurrences.Fixed -> {
                parcel.writeInt(CategoryType.Fixed.ordinal)
                FixedRecurrencesParceler.run {
                    write(parcel, flags)
                }
            }

            is Recurrences.Seasonal -> {
                parcel.writeInt(CategoryType.Seasonal.ordinal)
                SeasonalRecurrencesParceler.run {
                    write(parcel, flags)
                }
            }

            is Recurrences.Variable -> {
                parcel.writeInt(CategoryType.Variable.ordinal)
            }
        }
    }
}

private object FixedRecurrencesParceler : Parceler<Recurrences.Fixed> {
    override fun create(parcel: Parcel): Recurrences.Fixed {
        val daysOfMonth = DayOfMonth(parcel.readInt())
        return Recurrences.Fixed(daysOfMonth)
    }

    override fun Recurrences.Fixed.write(parcel: Parcel, flags: Int) {
        parcel.writeInt(day.value)
    }
}

private object SeasonalRecurrencesParceler : Parceler<Recurrences.Seasonal> {
    override fun create(parcel: Parcel): Recurrences.Seasonal {
        val daysAndMonths = parcel.createIntArray()
            ?.asSequence()
            ?.chunked(2)
            ?.map { (day, month) ->
                DayAndMonth(DayOfMonth(day), Month(month))
            }
            ?.toList()
            .orEmpty()
        return Recurrences.Seasonal(daysAndMonths)
    }

    override fun Recurrences.Seasonal.write(parcel: Parcel, flags: Int) {
        val intArray = IntArray(daysAndMonths.size * 2)
        daysAndMonths.forEachIndexed { index, dayAndMonth ->
            intArray[index * 2] = dayAndMonth.day.value
            intArray[index * 2 + 1] = dayAndMonth.month.value
        }
        parcel.writeIntArray(intArray)
    }
}
