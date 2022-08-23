package br.com.mob1st.bet.core.timber

import android.util.Log
import br.com.mob1st.bet.core.firebase.FirebaseExceptionDebugger
import br.com.mob1st.bet.core.logs.getPropertiesTree
import br.com.mob1st.bet.core.utils.toFormat
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap
import timber.log.Timber
import java.util.Date

class CrashReportingTree(
    private val crashlytics: FirebaseCrashlytics,
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }
        setCustomProperties(priority, t)
        crashlytics.log(message)
        if (t != null) {
            crashlytics.recordException(t)
        }
    }

    private fun setCustomProperties(priority: Int, throwable: Throwable?) {
        crashlytics.setCustomKey(LOG_PRIORITY_KEY, priority)
        throwable?.getPropertiesTree(FirebaseExceptionDebugger)?.let { map ->
            crashlytics.setCustomKeys(map.toCustomKeyValues().build())
        }
    }

    companion object {
        private const val LOG_PRIORITY_KEY = "LOG_PRIORITY_KEY"
    }
}

private fun Map<String, Any>.toCustomKeyValues(
    builder: CustomKeysAndValues.Builder = CustomKeysAndValues.Builder()
): CustomKeysAndValues.Builder {

    entries.forEach { property ->
        when (val value = property.value) {
            is String -> builder.putString(property.key, value)
            is Int -> builder.putInt(property.key, value)
            is Double -> builder.putDouble(property.key, value)
            is Long -> builder.putLong(property.key, value)
            is Float -> builder.putFloat(property.key, value)
            is Boolean -> builder.putBoolean(property.key, value)
            is Date -> builder.putString(property.key, value.toFormat())
            else -> {
                @OptIn(ExperimentalSerializationApi::class)
                Properties.encodeToMap(value).toCustomKeyValues(builder)
            }
        }
    }
    return builder
}

