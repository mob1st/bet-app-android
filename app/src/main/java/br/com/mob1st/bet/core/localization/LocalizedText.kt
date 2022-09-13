package br.com.mob1st.bet.core.localization

import android.content.Context
import androidx.core.os.ConfigurationCompat

/**
 * A key-value group of words, where the key is the language and the value is the localized text
 */
typealias LocalizedText = Map<String, String>

val LocalizedText.default: String get() = getOrDefault("en-us", checkNotNull(get(keys.first())))

fun Context.getText(text: LocalizedText): String {
    val locales = ConfigurationCompat.getLocales(resources.configuration)
    return text.getOrDefault(locales.toLanguageTags(), text.default)
}
