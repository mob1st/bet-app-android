package br.com.mob1st.features.utils.errors

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

internal fun Context.launchNetworkSettings() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.setClassName(
        "com.android.phone",
        "com.android.phone.NetworkSetting",
    )
    ContextCompat.startActivity(this, intent, null)
}
