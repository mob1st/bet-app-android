package br.com.mob1st.core.design.atoms.properties.texts

import android.content.res.Resources
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import kotlinx.parcelize.Parcelize

/**
 * A text that can be resolved to a string.
 * It can be a simple actual string or a localized string resource, for example.
 */
sealed interface TextState : Parcelable {
    /**
     * Resolves the text state to an [AnnotatedString].
     */
    fun resolve(resources: Resources): String
}

/**
 * Remembers the resolved string of the given [TextState].
 * @param text The text state to remember.
 * @return The resolved string.
 */
@Composable
fun rememberTextState(text: TextState): String {
    val resources = LocalContext.current.resources
    return remember(text) {
        text.resolve(resources)
    }
}

/**
 * Creates a text state from the given [string].
 * @param string The string.
 * @return The text state.
 */
fun TextState(string: String): TextState = ActualString(string)

/**
 * Creates a text state from the given string resource [id].
 * @param id The string resource id.
 * @param parameters The parameters to format the string.
 * @return The text state.
 */
fun TextState(
    @StringRes id: Int,
    parameters: List<TextState> = emptyList(),
): TextState {
    return if (parameters.isEmpty()) {
        ResourceString(id)
    } else {
        ParameterizedResourceString(id, parameters)
    }
}

/**
 * Creates a text state from the given plurals resource [id].
 * @param id The plurals resource id.
 * @param quantity The quantity to resolve the string.
 * @param parameters The parameters to format the string.
 * @return The text state.
 */
fun TextState(
    @PluralsRes id: Int,
    quantity: Int,
    parameters: List<TextState> = emptyList(),
): TextState {
    return PluralString(id, quantity, parameters)
}

@JvmInline
@Parcelize
private value class ActualString(private val value: String) : TextState {
    override fun resolve(resources: Resources): String = value

    override fun toString(): String {
        return value
    }
}

@Parcelize
private data class ParameterizedResourceString(
    val value: Int,
    val parameters: List<TextState>,
) : TextState {
    @Suppress("SpreadOperator")
    override fun resolve(resources: Resources): String {
        val params = parameters.toStrings(resources)
        return resources.getString(value, *params)
    }
}

@JvmInline
@Parcelize
private value class ResourceString(
    @StringRes val id: Int,
) : TextState {
    override fun resolve(resources: Resources): String {
        return resources.getString(id)
    }
}

@Parcelize
private data class PluralString(
    @PluralsRes val id: Int,
    val quantity: Int,
    val parameters: List<TextState>,
) : TextState {
    override fun resolve(resources: Resources): String {
        val params = parameters.toStrings(resources)
        params.copyOf(params.size)
        @Suppress("SpreadOperator")
        return resources.getQuantityString(id, quantity, *params)
    }
}

private fun List<TextState>.toStrings(resources: Resources): Array<String> {
    return map { it.resolve(resources) }.toTypedArray()
}
