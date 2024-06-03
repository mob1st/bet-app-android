package br.com.mob1st.core.design.molecules.buttons

import androidx.compose.animation.Crossfade
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.icons.IconState
import br.com.mob1st.core.design.atoms.properties.texts.TextState

@Immutable
sealed interface ButtonState {
    val isLoading: Boolean
    val isEnabled: Boolean

    fun isActive() = isEnabled && !isLoading

    @Immutable
    data class Default(
        val text: TextState,
        val icon: ButtonIcon? = null,
        override val isLoading: Boolean = false,
        override val isEnabled: Boolean = true,
    ) : ButtonState

    @Immutable
    interface Icon : ButtonState {
        val icon: IconState

        companion object {
            fun Add(
                icon: IconState,
                isLoading: Boolean = false,
                isEnabled: Boolean = true,
            ): Icon =
                IconButtonImpl(
                    icon = icon,
                    isLoading = isLoading,
                    isEnabled = isEnabled,
                )
        }
    }
}

@JvmInline
@Immutable
value class ButtonIcon private constructor(val icon: IconState) {
    companion object {
        val Add = ButtonIcon(
            icon = IconState.Add,
        )
    }
}

@Immutable
private data class IconButtonImpl(
    override val icon: IconState,
    override val isLoading: Boolean = false,
    override val isEnabled: Boolean = true,
) : ButtonState.Icon

@Composable
private fun LoadingAnimation(
    isLoading: Boolean,
    content: @Composable () -> Unit,
) {
    Crossfade(targetState = isLoading, label = "buttonCrossfade") { target ->
        if (target) {
            CircularProgressIndicator()
        } else {
            content()
        }
    }
}
