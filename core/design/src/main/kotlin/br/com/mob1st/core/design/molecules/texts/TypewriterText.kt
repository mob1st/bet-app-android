package br.com.mob1st.core.design.molecules.texts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.delay

private const val DELAY_PER_NON_FINAL_CHUNK = 160L
private const val DELAY_PER_FINAL_CHUNK = 200L
private const val PER_CHAR_DELAY = 80L

@Composable
fun TypewriterText(state: TypewriterTextState) {
    // ensures that the animation is not restarted when the configuration changes
    var shouldRunAnimation by rememberSaveable {
        mutableStateOf(true)
    }

    if (shouldRunAnimation) {
        AnimatedText(
            state = state,
            onAnimationEnd = {
                shouldRunAnimation = false
            },
        )
    } else {
        Text(text = state.text)
    }
}

@Composable
private fun AnimatedText(
    state: TypewriterTextState,
    onAnimationEnd: () -> Unit,
) {
    // hold the initial text. it's empty by default or it has the last state before the configuration change
    var initialText by rememberSaveable {
        mutableStateOf(AnnotatedString(""))
    }

    var textToDisplay by remember {
        // starts with the initial text and then it's updated by the type function
        mutableStateOf(initialText)
    }

    LaunchedEffect(state) {
        type(
            state = state,
            initialIndex = initialText.length,
            onTypeChar = { newText ->
                // restore typing from the last state before the configuration change
                textToDisplay =
                    if (textToDisplay.isEmpty()) {
                        initialText + newText
                    } else {
                        newText
                    }

                // save the current state to be able to restore it in case of a configuration change
                initialText = newText
            },
        )
        onAnimationEnd()
    }

    Text(text = textToDisplay)
}

private suspend fun type(
    state: TypewriterTextState,
    initialIndex: Int,
    onTypeChar: (AnnotatedString) -> Unit,
) {
    for (index in state.text.indices) {
        onTypeChar(state.text.subSequence(initialIndex, index))
        state.pauses[index]?.let {
            it()
        } ?: delay(PER_CHAR_DELAY)
    }
}

private suspend inline operator fun TypewriterTextState.Pause.invoke() {
    val delay =
        when (this) {
            TypewriterTextState.Pause.LONG -> DELAY_PER_FINAL_CHUNK
            TypewriterTextState.Pause.SHORT -> DELAY_PER_NON_FINAL_CHUNK
        }
    delay(delay)
}

/**
 * A type writer style text.
 * It's used to add a human touch to the text, where the text is displayed character by character, simulating a person
 * typing the text.
 * @property pauses The pauses to be applied in the animation. It's a map where the key is the index of the character
 * and the value is the pause to be applied.
 * @property text The full text to be displayed.
 */
@Immutable
data class TypewriterTextState(
    val pauses: ImmutableMap<Int, Pause>,
    val text: AnnotatedString,
) {
    enum class Pause {
        SHORT,
        LONG,
    }
}
