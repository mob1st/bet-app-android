package br.com.mob1st.core.design.organisms.lists

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.Icon
import br.com.mob1st.core.design.atoms.properties.Text
import br.com.mob1st.core.design.molecules.avatar.AvatarState

@Immutable
data class ListItemState(
    val headline: Text,
    val supporting: Text? = null,
    val leading: AvatarState? = null,
    val trailing: Trailing? = null,
) {

    @Immutable
    sealed interface Trailing

    @JvmInline
    value class SupportingText(val value: Text) : Trailing

    @JvmInline
    value class ActionIcon(val icon: Icon) : Trailing

    data object Loading : Trailing
}
