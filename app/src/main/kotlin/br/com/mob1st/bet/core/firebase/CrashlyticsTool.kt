package br.com.mob1st.bet.core.firebase

import br.com.mob1st.bet.core.arrow.dateTimeIso
import br.com.mob1st.bet.core.logs.CrashReportingTool
import br.com.mob1st.bet.core.logs.Debuggable
import br.com.mob1st.bet.core.logs.getPropertiesTree
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.util.Date

class CrashlyticsTool(
    private val crashlytics: FirebaseCrashlytics,
) : CrashReportingTool {
    override fun registerUser(userId: String) {
        crashlytics.setUserId(userId)
    }

    override fun clearUser() {
        crashlytics.setUserId("")
    }

    override fun log(
        message: String,
        throwable: Throwable?,
    ) {
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
            is Date -> builder.putString(key, dateTimeIso.get(value))
            is Debuggable -> value.logProperties().toCustomKeyValues(builder)
            else -> builder.putString(key, value.toString())
        }
    }
    return builder
}
