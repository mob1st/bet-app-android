package br.com.mob1st.bet.core.arrow

import arrow.optics.Iso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

val dateTimeIso: Iso<Date, String> =
    Iso(
        get = {
            SimpleDateFormat(ISO_DATE_TIME, Locale.getDefault())
                .format(it)
        },
        reverseGet = {
            val sdf = SimpleDateFormat(ISO_DATE_TIME, Locale.getDefault())
            checkNotNull(sdf.parse(it))
        },
    )
