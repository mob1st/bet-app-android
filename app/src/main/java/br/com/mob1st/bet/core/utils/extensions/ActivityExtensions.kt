package br.com.mob1st.bet.core.utils.extensions

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable

/**
 * Creates the intent to start a new Activity
 *
 * Uses the [intentBlock] to customize the intent as needed
 */
inline fun <reified T : Activity> Activity.intent(
    intentBlock: Intent.() -> Unit = {},
): Intent {
    val intent = Intent(this, T::class.java)
    intent.intentBlock()
    return intent
}

/**
 * Start the current [Activity] with the given [Intent]
 */
context(Activity)
fun Intent.start(options: Bundle? = null) {
    startActivity(this, options)
}

inline fun <reified T : Parcelable> Intent.getParcelableNotNull(name: String): T {
    val parcelable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(name)
    }
    return requireNotNull(parcelable) {
        "The parcelable extra $name must be provided with an instance of ${T::class.simpleName}"
    }
}