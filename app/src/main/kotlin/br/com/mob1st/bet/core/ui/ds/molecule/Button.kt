package br.com.mob1st.bet.core.ui.ds.molecule

import androidx.annotation.DrawableRes
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.mob1st.bet.core.tooling.androidx.TextData
import br.com.mob1st.bet.core.tooling.androidx.resolve
import br.com.mob1st.bet.core.ui.ds.atoms.IconState
import br.com.mob1st.bet.core.ui.ds.atoms.ReplaceAnimation
import br.com.mob1st.bet.core.ui.ds.atoms.VisibilityAnimation
import javax.annotation.concurrent.Immutable

@Immutable
sealed interface ButtonState {
    val enabled: Boolean
    val loading: Boolean

    fun isAllowingClicks() = !loading && enabled

    data class Primary(
        val text: TextData,
        @DrawableRes val trailing: Int? = null,
        override val loading: Boolean = false,
        override val enabled: Boolean = true,
    ) : ButtonState

    data class Icon(
        val icon: IconState,
        override val enabled: Boolean = true,
        override val loading: Boolean = false,
    ) : ButtonState
}

@Composable
fun PrimaryButton(
    state: ButtonState.Primary,
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        TextArea(state = state)
    }
}

@Composable
fun TextButton(
    state: ButtonState.Primary,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = state.isAllowingClicks(),
    ) {
        TextArea(state = state)
    }
}

@Composable
private fun TextArea(state: ButtonState.Primary) {
    Text(text = state.text.resolve())
    if (state.trailing != null) {
        ReplaceAnimation(targetState = state.loading) {
            if (it) {
                CircularProgressIndicator()
            } else {
                state.trailing.let { id ->
                    Icon(painter = painterResource(id), contentDescription = null)
                }
            }
        }
    } else {
        VisibilityAnimation(visible = state.loading) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    state: ButtonState.Icon,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        enabled = state.isAllowingClicks(),
        onClick = onClick,
    ) {
        ReplaceAnimation(targetState = state.loading) {
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                Icon(
                    painter = painterResource(state.icon.resId),
                    contentDescription = state.icon.contentDescription?.resolve(),
                )
            }
        }
    }
}

object ButtonDefaults {
    val height: Dp = 64.dp

    object Icon
}
