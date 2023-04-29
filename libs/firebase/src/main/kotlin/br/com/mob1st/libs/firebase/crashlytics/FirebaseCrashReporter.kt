package br.com.mob1st.libs.firebase.crashlytics

import br.com.mob1st.core.observability.crashes.CrashReporter
import br.com.mob1st.core.observability.crashes.getRootLogProperties
import br.com.mob1st.core.observability.debug.Debuggable
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.core.annotation.Factory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Crash reporting tool implementation for Firebase Crashlytics.
 */
@Factory
class FirebaseCrashReporter(
    private val crashlytics: FirebaseCrashlytics,
) : CrashReporter {

    override fun crash(throwable: Throwable) {
        val logProperties = throwable.getRootLogProperties()
        val customKeysAndValues = logProperties.toCustomKeyValues().build()
        crashlytics.setCustomKeys(customKeysAndValues)
        crashlytics.recordException(throwable)
    }
}

private fun Map<String, Any?>.toCustomKeyValues(
    builder: CustomKeysAndValues.Builder = CustomKeysAndValues.Builder(),
): CustomKeysAndValues.Builder {
    entries.forEach { (key, value) ->
        when (value) {
            is String -> builder.putString(key, value)
            is Int -> builder.putInt(key, value)
            is Double -> builder.putDouble(key, value)
            is Long -> builder.putLong(key, value)
            is Float -> builder.putFloat(key, value)
            is Boolean -> builder.putBoolean(key, value)
            is Date -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
                builder.putString(key, sdf.format(value))
            }
            is Debuggable -> value.logInfo.toCustomKeyValues(builder)
            else -> builder.putString(key, value.toString())
        }
    }
    return builder
}
