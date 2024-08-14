package br.com.mob1st.features.finances.impl.ui.category.navigation

import android.os.Parcel
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import kotlinx.parcelize.Parceler

/**
 * Parcels the domain object [GetCategoryIntent].
 */
object GetCategoryIntentParceler : Parceler<GetCategoryIntent> {
    override fun create(parcel: Parcel): GetCategoryIntent {
        return if (parcel.readInt() == 0) {
            GetCategoryIntent.Create(checkNotNull(parcel.readString()))
        } else {
            GetCategoryIntent.Edit(
                id = Category.Id(parcel.readLong()),
                name = checkNotNull(parcel.readString()),
            )
        }
    }

    override fun GetCategoryIntent.write(parcel: Parcel, flags: Int) {
        when (this) {
            is GetCategoryIntent.Edit -> {
                parcel.writeInt(1)
                parcel.writeLong(id.value)
            }

            is GetCategoryIntent.Create -> parcel.writeInt(0)
        }
        parcel.writeString(name)
    }
}
