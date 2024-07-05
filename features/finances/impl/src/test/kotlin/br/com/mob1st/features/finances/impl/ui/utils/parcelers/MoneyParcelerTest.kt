package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mob1st.core.kotlinx.structures.Money
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class MoneyParcelerTest {
    @Test
    fun `GIVEN a parcel WHEN write money THEN assert cents is serialized`() {
        val money = Money(100)
        val parcel = Parcel.obtain()
        MoneyParceler.run {
            money.write(parcel, 0)
        }
        parcel.setDataPosition(0)
        assertEquals(100, parcel.readLong())
    }

    @Test
    fun `GIVEN a parcel WHEN create money THEN assert cents is deserialized`() {
        val parcel = Parcel.obtain()
        parcel.writeLong(100)
        parcel.setDataPosition(0)
        val money = MoneyParceler.create(parcel)
        assertEquals(100, money.cents)
    }
}
