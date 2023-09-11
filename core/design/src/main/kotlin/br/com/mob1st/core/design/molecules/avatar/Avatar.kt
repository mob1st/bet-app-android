package br.com.mob1st.core.design.molecules.avatar

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImage

@Composable
fun Avatar(state: AvatarState, contentDescription: String? = null, modifier: Modifier = Modifier) {
    when (state) {
        is AvatarState.Dynamic -> TODO()
        is AvatarState.Initials -> TODO()
        is AvatarState.Static -> TODO()
    }
}

@Composable
private fun DynamicAvatar(state: AvatarState.Dynamic, contentDescription: String?, modifier: Modifier) {
    AsyncImage(
        model = state.url,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@Composable
private fun InitialsAvatar(state: AvatarState.Initials, contentDescription: String?, modifier: Modifier) {
    Canvas(
        modifier = modifier.semantics {
            role = Role.Image
            this.contentDescription = contentDescription ?: ""
        }
    ) {
        TODO()
    }
}
