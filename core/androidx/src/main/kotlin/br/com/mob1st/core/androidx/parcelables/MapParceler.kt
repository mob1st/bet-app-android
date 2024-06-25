package br.com.mob1st.core.androidx.parcelables

import android.os.Parcel
import kotlinx.parcelize.Parceler

object MapParceler : Parceler<Map<String, String>> {
    override fun create(parcel: Parcel): Map<String, String> {
        TODO("Not yet implemented")
    }

    override fun Map<String, String>.write(
        parcel: Parcel,
        flags: Int,
    ) = Unit
}
