package br.com.mob1st.core.design.organisms.navigationbar

import androidx.compose.runtime.Immutable
import br.com.mob1st.core.design.atoms.properties.icons.IconState
import br.com.mob1st.core.design.atoms.properties.texts.LocalizedText

@Immutable
data class NavigationBarState(
    val items: List<NavBarItem>,
)

@Immutable
data class NavBarItem(
    val text: LocalizedText,
    val icon: IconState,
    val isSelected: Boolean,
)
