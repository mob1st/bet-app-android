package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mob1st.features.finances.impl.domain.entities.RecurrenceType
import br.com.mob1st.features.finances.impl.domain.entities.Recurrences
import br.com.mob1st.features.finances.impl.domain.values.DayOfMonth
import br.com.mob1st.features.finances.impl.domain.values.DayOfYear
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecurrencesParcelerTest {
    @Test
    fun `GIVEN a parcel WHEN write fixed recurrence THEN assert recurrences are serialized`() {
        val parcel = Parcel.obtain()
        val recurrences = Recurrences.Fixed(DayOfMonth(1))
        RecurrencesParceler.run {
            recurrences.write(parcel, 0)
        }
        parcel.setDataPosition(0)
        assertEquals(RecurrenceType.Fixed.ordinal, parcel.readInt())
        assertEquals(1, parcel.readInt())
    }

    @Test
    fun `GIVEN a parcel WHEN create fixed recurrence THEN assert recurrences are deserialized`() {
        val parcel = Parcel.obtain()
        parcel.writeInt(RecurrenceType.Fixed.ordinal)
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
                DayOfYear(200),
                DayOfYear(1),
            ),
        )
        RecurrencesParceler.run {
            recurrences.write(parcel, 0)
        }
        parcel.setDataPosition(0)
        // read the identifier
        parcel.readInt()

        val array = IntArray(2)
        parcel.readIntArray(array)

        assertEquals(200, array[0])
        assertEquals(1, array[1])
    }

    @Test
    fun `GIVEN a parcel WHEN create seasonal recurrence THEN assert recurrences are deserialized`() {
        val parcel = Parcel.obtain()
        parcel.writeInt(RecurrenceType.Seasonal.ordinal)
        val array = intArrayOf(10, 20)
        parcel.writeIntArray(array)
        parcel.setDataPosition(0)
        val recurrences = RecurrencesParceler.create(parcel)
        assertEquals(
            Recurrences.Seasonal(
                listOf(
                    DayOfYear(10),
                    DayOfYear(20),
                ),
            ),
            recurrences,
        )
    }
}
