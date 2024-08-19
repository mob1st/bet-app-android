package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import br.com.mob1st.core.androidx.parcelables.readParcelableAs
import br.com.mob1st.core.kotlinx.structures.Uri
import kotlinx.parcelize.Parceler

private typealias AndroidUri = android.net.Uri

/**
 * Parceler for [Uri].
 */
internal object UriParceler : Parceler<Uri> {
    override fun create(parcel: Parcel): Uri {
        val androidUri = checkNotNull(parcel.readParcelableAs<AndroidUri>())
        val string = checkNotNull(androidUri.toString())
        return Uri(string)
    }

    override fun Uri.write(parcel: Parcel, flags: Int) {
        val uri = AndroidUri.parse(this.value)
        parcel.writeParcelable(uri, flags)
    }
}
