package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import br.com.mob1st.core.kotlinx.structures.Money
import kotlinx.parcelize.Parceler

/**
 * Parcels a [Money] object.
 */
object MoneyParceler : Parceler<Money> {
    override fun create(parcel: Parcel): Money {
        return Money(parcel.readLong())
    }

    override fun Money.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(cents)
    }
}
