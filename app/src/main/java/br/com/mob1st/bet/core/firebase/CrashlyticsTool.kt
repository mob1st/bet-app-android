package br.com.mob1st.bet.core.firebase

import br.com.mob1st.bet.core.logs.CrashReportingTool
import br.com.mob1st.bet.core.logs.getPropertiesTree
import br.com.mob1st.bet.core.utils.toFormat
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.core.annotation.Factory
import java.util.Date

@Factory
class CrashlyticsTool(
    private val crashlytics: FirebaseCrashlytics
) : CrashReportingTool {
    override fun registerUser(userId: String) {
        crashlytics.setUserId(userId)
    }

    override fun clearUser() {
        crashlytics.setUserId("")
    }

    override fun log(message: String, throwable: Throwable?) {
        crashlytics.log(message)
        if (throwable != null) {
            setCustomProperties(throwable)
            crashlytics.recordException(throwable)
        }
    }

    private fun setCustomProperties(throwable: Throwable) {
        val map = throwable.getPropertiesTree(FirebaseExceptionDebugger)
        crashlytics.setCustomKeys(map.toCustomKeyValues().build())
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
            else -> builder.putString(property.key, property.value.toString())
        }
    }
    return builder
}