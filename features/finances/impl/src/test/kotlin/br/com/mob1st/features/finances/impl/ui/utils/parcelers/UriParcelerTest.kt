package br.com.mob1st.features.finances.impl.ui.utils.parcelers

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mob1st.core.androidx.parcelables.readParcelableAs
import br.com.mob1st.core.kotlinx.structures.Uri
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UriParcelerTest {
    @Test
    fun `GIVEN a parcel WHEN write THEN assert URI is written as string`() {
        val parcel = Parcel.obtain()
        val uri = Uri("https://www.google.com")
        UriParceler.run {
            uri.write(parcel, 0)
        }
        parcel.setDataPosition(0)
        val androidUri = parcel.readParcelableAs<android.net.Uri>()
        assertEquals(android.net.Uri.parse("https://www.google.com"), androidUri)
    }

    @Test
    fun `GIVEN a parcel WHEN create THEN assert URI is read as string`() {
        val parcel = Parcel.obtain()
        val androidUri = android.net.Uri.parse("https://www.google.com")
        parcel.writeParcelable(androidUri, 0)
        parcel.setDataPosition(0)
        val uri = UriParceler.create(parcel)
        assertEquals(Uri("https://www.google.com"), uri)
    }
}
