package br.com.mob1st.core.design.atoms.properties.texts

import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize

/**
 * A text that can be resolved to a string.
 * It can be a simple actual string or a localized string resource, for example.
 */
@Immutable
interface TextState : Parcelable {
    /**
     * Resolves the text to a string.
     */
    @Composable
    fun resolve(): String
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
@Immutable
private value class ActualString(private val value: String) : TextState {
    @Composable
    override fun resolve(): String = value

    override fun toString(): String {
        return value
    }
}

@Parcelize
@Immutable
private data class ParameterizedResourceString(
    val value: Int,
    val parameters: List<TextState>,
) : TextState {
    @Suppress("SpreadOperator")
    @Composable
    override fun resolve(): String {
        val params = parameters.toStrings()
        return stringResource(value, *params)
    }
}

@JvmInline
@Parcelize
private value class ResourceString(
    @StringRes val id: Int,
) : TextState {
    @Composable
    override fun resolve(): String {
        return stringResource(id = id)
    }
}

@Parcelize
private data class PluralString(
    @PluralsRes val id: Int,
    val quantity: Int,
    val parameters: List<TextState>,
) : TextState {
    @Composable
    override fun resolve(): String {
        val params = parameters.toStrings()
        @Suppress("SpreadOperator")
        return pluralStringResource(id, quantity, *params)
    }
}

@Composable
private fun List<TextState>.toStrings(): Array<String> {
    return map { it.resolve() }.toTypedArray()
}
