package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayAndMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.Month
import br.com.mob1st.features.finances.publicapi.domain.entities.CategoryType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RecurrencesParcelerTest {
    @Test
    fun `GIVEN a parcel WHEN write fixed recurrence THEN assert recurrences are serialized`() {
        val parcel = Parcel.obtain()
        val recurrences = Recurrences.Fixed(DayOfMonth(1))
        RecurrencesParceler.run {
            recurrences.write(parcel, 0)
        }
        parcel.setDataPosition(0)
        assertEquals(CategoryType.Fixed.ordinal, parcel.readInt())
        assertEquals(1, parcel.readInt())
    }

    @Test
    fun `GIVEN a parcel WHEN create fixed recurrence THEN assert recurrences are deserialized`() {
        val parcel = Parcel.obtain()
        parcel.writeInt(CategoryType.Fixed.ordinal)
        parcel.writeInt(1)
        parcel.setDataPosition(0)
        val recurrences = RecurrencesParceler.create(parcel)
        assertEquals(Recurrences.Fixed(DayOfMonth(1)), recurrences)
    }

    @Test
    fun `GIVEN a parcel WHEN write seasonal recurrence THEN assert recurrences are serialized`() {
        val parcel = Parcel.obtain()
        val recurrences = Recurrences.Seasonal(
            listOf(
                DayAndMonth(DayOfMonth(1), Month.February),
                DayAndMonth(DayOfMonth(31), Month.December),
            ),
        )
        RecurrencesParceler.run {
            recurrences.write(parcel, 0)
        }
        parcel.setDataPosition(0)
        val categoryType = CategoryType.entries[parcel.readInt()]
        val array = IntArray(4)
        parcel.readIntArray(array)

        assertEquals(CategoryType.Seasonal, categoryType)
        assertEquals(1, array[0])
        assertEquals(Month.February.value, array[1])
        assertEquals(31, array[2])
        assertEquals(Month.December.value, array[3])
    }

    @Test
    fun `GIVEN a parcel WHEN create seasonal recurrence THEN assert recurrences are deserialized`() {
        val parcel = Parcel.obtain()
        parcel.writeInt(CategoryType.Seasonal.ordinal)
        val array = intArrayOf(10, Month.March.value, 20, Month.November.value)
        parcel.writeIntArray(array)
        parcel.setDataPosition(0)
        val recurrences = RecurrencesParceler.create(parcel)
        assertEquals(
            Recurrences.Seasonal(
                listOf(
                    DayAndMonth(DayOfMonth(10), Month.March),
                    DayAndMonth(DayOfMonth(20), Month.November),
                ),
            ),
            recurrences,
        )
    }
}
