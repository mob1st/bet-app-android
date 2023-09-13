package br.com.mob1st.core.design.atoms.properties

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

sealed interface Text {

    fun resolve(resources: Resources): AnnotatedString
}

@Composable
fun rememberAnnotatedString(text: Text): AnnotatedString {
    val resources = LocalContext.current.resources
    return remember(text) {
        text.resolve(resources)
    }
}

fun Text(string: String): Text = ActualString(string)
fun Text(@StringRes id: Int, parameters: List<Text> = emptyList()): Text {
    return if (parameters.isEmpty()) {
        ResourceString(id)
    } else {
        ParameterizedResourceString(id, parameters)
    }
}

fun Text(@PluralsRes id: Int, quantity: Int, parameters: List<Text> = emptyList()): Text {
    return PluralString(id, quantity, parameters)
}

fun Text(fullText: Text, ranges: List<Pair<StyleType, IntRange>>): Text {
    return StyledText(fullText, ranges)
}

@JvmInline
private value class ActualString(private val value: String) : Text {
    override fun resolve(resources: Resources): AnnotatedString = AnnotatedString(value)
}

private data class ParameterizedResourceString(
    val value: Int,
    val parameters: List<Text>,
) : Text {

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
) : Text {
    override fun resolve(resources: Resources): AnnotatedString {
        return resources.getString(id).let(::AnnotatedString)
    }
}

private data class PluralString(
    @PluralsRes val id: Int,
    val quantity: Int,
    val parameters: List<Text>,
) : Text {
    override fun resolve(resources: Resources): AnnotatedString {
        val params = parameters.toStrings(resources)
        params.copyOf(params.size)
        @Suppress("SpreadOperator")
        return resources.getQuantityString(id, quantity, *params).let(::AnnotatedString)
    }
}

private data class StyledText(
    val fullText: Text,
    val ranges: List<Pair<StyleType, IntRange>>,
) : Text {
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
            StyleType.Link -> SpanStyle(
                color = resources.getLinkColor(),
                fontWeight = FontWeight.Bold
            )
            StyleType.Bold -> SpanStyle(
                fontWeight = FontWeight.Bold
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

private fun List<Text>.toStrings(resources: Resources): Array<String> {
    return map { it.resolve(resources).text }.toTypedArray()
}
