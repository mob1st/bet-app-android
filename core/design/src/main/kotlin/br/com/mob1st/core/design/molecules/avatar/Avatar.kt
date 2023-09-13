package br.com.mob1st.core.design.molecules.avatar

import androidx.compose.runtime.Composable

@Composable
fun Avatar(state: AvatarState) {
    when (state) {
        is AvatarState.Dynamic -> TODO()
        is AvatarState.Initials -> TODO()
        is AvatarState.Static -> TODO()
    }
}
