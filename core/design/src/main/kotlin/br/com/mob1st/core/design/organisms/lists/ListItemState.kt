package br.com.mob1st.core.design.organisms.lists

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.icons.IconState
import br.com.mob1st.core.design.atoms.properties.texts.TextState
import br.com.mob1st.core.design.molecules.avatar.AvatarState

@Immutable
data class ListItemState(
    val headline: TextState,
    val supporting: TextState? = null,
    val leading: AvatarState? = null,
    val trailing: Trailing? = null,
) {
    @Immutable
    sealed interface Trailing

    @JvmInline
    value class Text(val value: TextState) : Trailing

    @JvmInline
    value class ActionIcon(val icon: IconState) : Trailing

    data object Loading : Trailing
}
