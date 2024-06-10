package br.com.mob1st.core.design.atoms.properties.texts

import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.util.fastForEach

sealed interface TextState {
    fun resolve(resources: Resources): AnnotatedString
}

@Composable
fun rememberAnnotatedString(text: TextState): AnnotatedString {
    val resources = LocalContext.current.resources
    return remember(text) {
        text.resolve(resources)
    }
}

fun TextState(string: String): TextState = ActualString(string)

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

fun TextState(
    @PluralsRes id: Int,
    quantity: Int,
    parameters: List<TextState> = emptyList(),
): TextState {
    return PluralString(id, quantity, parameters)
}

fun TextState(
    fullText: TextState,
    ranges: List<Pair<StyleType, IntRange>>,
): TextState {
    return StyledText(fullText, ranges)
}

@JvmInline
private value class ActualString(private val value: String) : TextState {
    override fun resolve(resources: Resources): AnnotatedString = AnnotatedString(value)

    override fun toString(): String {
        return value
    }
}

private data class ParameterizedResourceString(
    val value: Int,
    val parameters: List<TextState>,
) : TextState {
    @Suppress("SpreadOperator")
    override fun resolve(resources: Resources): AnnotatedString {
        val params = parameters.toStrings(resources)
        val string = resources.getString(value, *params)
        return AnnotatedString(string)
    }
}

@JvmInline
private value class ResourceString(
    @StringRes val id: Int,
) : TextState {
    override fun resolve(resources: Resources): AnnotatedString {
        return resources.getString(id).let(::AnnotatedString)
    }
}

private data class PluralString(
    @PluralsRes val id: Int,
    val quantity: Int,
    val parameters: List<TextState>,
) : TextState {
    override fun resolve(resources: Resources): AnnotatedString {
        val params = parameters.toStrings(resources)
        params.copyOf(params.size)
        @Suppress("SpreadOperator")
        return resources.getQuantityString(id, quantity, *params).let(::AnnotatedString)
    }
}

private data class StyledText(
    val fullText: TextState,
    val ranges: List<Pair<StyleType, IntRange>>,
) : TextState {
    override fun resolve(resources: Resources): AnnotatedString {
        val text = fullText.resolve(resources)
        return buildAnnotatedString {
            append(text)
            ranges.fastForEach { (style, range) ->
                addStyle(style.toSpanStyle(resources), range.first, range.last)
            }
        }
    }

    private fun StyleType.toSpanStyle(resources: Resources): SpanStyle {
        return when (this) {
            StyleType.Link ->
                SpanStyle(
                    color = resources.getLinkColor(),
                    fontWeight = FontWeight.Bold,
                )

            StyleType.Bold ->
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                )
        }
    }
}

enum class StyleType {
    Link,
    Bold,
}

private fun Resources.getLinkColor(): Color {
    val uiMode = configuration.uiMode
    val isDarkMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    return if (isDarkMode) {
        Color.Blue
    } else {
        Color.DarkGray
    }
}

private fun List<TextState>.toStrings(resources: Resources): Array<String> {
    return map { it.resolve(resources).text }.toTypedArray()
}
