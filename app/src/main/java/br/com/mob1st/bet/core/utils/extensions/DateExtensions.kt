package br.com.mob1st.bet.core.utils.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

fun Date.toFormat(format: String = ISO_DATE_TIME): String {
    return SimpleDateFormat(format, Locale.getDefault())
        .format(this)
}