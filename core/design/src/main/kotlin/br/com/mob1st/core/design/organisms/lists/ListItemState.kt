package br.com.mob1st.core.design.organisms.lists

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.icons.Icon
import br.com.mob1st.core.design.molecules.avatar.AvatarState

@Immutable
data class ListItemState(
    val headline: br.com.mob1st.core.design.atoms.properties.texts.Text,
    val supporting: br.com.mob1st.core.design.atoms.properties.texts.Text? = null,
    val leading: AvatarState? = null,
    val trailing: Trailing? = null,
) {

    @Immutable
    sealed interface Trailing

    @JvmInline
    value class Text(val value: br.com.mob1st.core.design.atoms.properties.texts.Text) : Trailing

    @JvmInline
    value class ActionIcon(val icon: Icon) : Trailing

    data object Loading : Trailing
}
